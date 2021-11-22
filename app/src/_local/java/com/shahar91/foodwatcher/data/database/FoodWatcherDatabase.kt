package com.shahar91.foodwatcher.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.dao.DayDescriptionDao
import com.shahar91.foodwatcher.data.dao.FavoriteFoodItemDao
import com.shahar91.foodwatcher.data.dao.FoodEntryDao
import com.shahar91.foodwatcher.data.dao.FoodItemDao
import com.shahar91.foodwatcher.data.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@Database(entities = [FoodItem::class, FoodEntry::class, FavoriteFoodItem::class, DayDescription::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FoodWatcherDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
    abstract fun foodEntryDao(): FoodEntryDao
    abstract fun favoriteFoodItemDao(): FavoriteFoodItemDao
    abstract fun dayDescriptionDao(): DayDescriptionDao

    companion object {
        private val mScope = CoroutineScope(SupervisorJob())

        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: FoodWatcherDatabase? = null

        fun getDatabase(context: Context): FoodWatcherDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, FoodWatcherDatabase::class.java, DBConstants.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(FoodWatcherDatabaseCallback(mScope))
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class FoodWatcherDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                scope.launch {
                    // To see an example of this to prepopulate a database, take a look at
                    // https://developer.android.com/codelabs/android-room-with-a-view-kotlin#13
                    val foodItemDao = database.foodItemDao().also { it.deleteAll() }
                    val foodEntryDao = database.foodEntryDao().also { it.deleteAll() }

                    val foodItemList = listOf(
                        FoodItem(name = "Bread (white, unsalted)", description = "1 slice", points = 0.5F),
                        FoodItem(name = "Rice", description = "100 grams", points = 3F),
                        FoodItem(name = "Pasta", description = "", points = 2F),
                        FoodItem(name = "Banana", description = "", points = 2F),
                        FoodItem(name = "Apple", description = "", points = 2F),
                        FoodItem(name = "Coffee", description = "", points = 2F),
                        FoodItem(name = "Zebra", description = "", points = 2F),
                        FoodItem(name = "Lamp", description = "", points = 2F),
                        FoodItem(name = "Home", description = "", points = 2F),
                        FoodItem(name = "Google", description = "", points = 2F),
                        FoodItem(name = "Ice Cream", description = "", points = 2F),
                        FoodItem(name = "Clock", description = "", points = 2F),
                        FoodItem(name = "Cheese", description = "", points = 2F),
                        FoodItem(name = "Dough", description = "", points = 2F),
                        FoodItem(name = "Nutella", description = "", points = 2F),
                        FoodItem(name = "Chocolate", description = "", points = 2F),
                        FoodItem(name = "Orange", description = "", points = 2F),
                        FoodItem(name = "Pineapple", description = "", points = 2F),
                        FoodItem(name = "Melon", description = "", points = 2F),
                        FoodItem(name = "Kiwi", description = "", points = 2F),
                        FoodItem(name = "Pumpkin", description = "", points = 2F),
                        FoodItem(name = "Parmigiano", description = "", points = 2F),
                        FoodItem(name = "Lasagna", description = "", points = 2F),
                        FoodItem(name = "Soup", description = "", points = 2F),
                        FoodItem(name = "Chicken", description = "", points = 2F),
                        FoodItem(name = "Veal", description = "", points = 2F),
                        FoodItem(name = "Pepper", description = "", points = 2F),
                        FoodItem(name = "Bread (brown, salted)", description = "1 slice", points = 3F)
                    ).also { foodItemDao.insertMany(it) }

                    val todayAtNoon = LocalDate.now().atTime(LocalTime.NOON)

                    foodEntryDao.insertMany(
                        listOf(
                            FoodEntry(
                                "1",
                                3F,
                                todayAtNoon.toEpochMilliTest(),
                                Meal.LUNCH,
                                "-1",
                                foodItemList[1].name,
                                foodItemList[1].description,
                                foodItemList[1].points
                            ),
                            FoodEntry(
                                "2",
                                1F,
                                todayAtNoon.toEpochMilliTest(),
                                Meal.BREAKFAST,
                                foodItemList[27].id,
                                foodItemList[27].name,
                                foodItemList[27].description,
                                foodItemList[27].points
                            ),
                            FoodEntry(
                                "3",
                                2F,
                                todayAtNoon.plusDays(1).toEpochMilliTest(),
                                Meal.DINNER,
                                foodItemList[2].id,
                                foodItemList[2].name,
                                foodItemList[2].description,
                                foodItemList[2].points
                            )
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