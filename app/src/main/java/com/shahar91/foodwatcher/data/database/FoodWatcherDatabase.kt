package com.shahar91.foodwatcher.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shahar91.foodwatcher.MyApp
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.dao.FoodEntryDao
import com.shahar91.foodwatcher.data.dao.FoodItemDao
import com.shahar91.foodwatcher.data.models.FavoriteFoodItem
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.data.models.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.*

@Database(entities = [FoodItem::class, FoodEntry::class, FavoriteFoodItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FoodWatcherDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
    abstract fun foodEntryDao(): FoodEntryDao

    companion object {
        private val scope = CoroutineScope(SupervisorJob())

        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: FoodWatcherDatabase? = null

        fun getDatabase(): FoodWatcherDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(MyApp.getContext(), FoodWatcherDatabase::class.java, DBConstants.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(FoodWatcherDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class FoodWatcherDatabaseCallback(scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                scope.launch {
                    val foodItemDao = database.foodItemDao().also { it.deleteAll() }
                    val foodEntryDao = database.foodEntryDao().also { it.deleteAll() }

                    val foodItemList = listOf(
                        FoodItem(id = 1, name = "Bread (white, unsalted)", description = "1 slice", points = 0.5F),
                        FoodItem(id = 2, name = "Rice", description = "100 grams", points = 3F),
                        FoodItem(id = 3, name = "Pasta", description = "100 grams", points = 2F),
                        FoodItem(id = 4, name = "Bread (brown, salted)", description = "1 slice", points = 3F)
                    ).also { foodItemDao.insertMany(it) }

                    val todayAtNoon = LocalDate.now().atTime(LocalTime.NOON)

                    foodEntryDao.insertMany(
                        listOf(
                            FoodEntry(foodItemId = foodItemList[0].id, amount = 3, date = todayAtNoon.toEpochMilliTest(), meal = Meal.LUNCH),
                            FoodEntry(foodItemId = foodItemList[2].id, amount = 2, date = todayAtNoon.plusDays(1).toEpochMilliTest(), meal = Meal.DINNER)
                        )
                    )
                }
            }
        }

        private fun LocalDateTime.toEpochMilliTest(): Long {
            return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }
}