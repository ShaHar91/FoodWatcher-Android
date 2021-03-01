package com.shahar91.foodwatcher.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shahar91.foodwatcher.MyApp
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.dao.FoodEntryDao
import com.shahar91.foodwatcher.data.dao.FoodItemDao
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.FoodItem

@Database(entities = [FoodItem::class, FoodEntry::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FoodWatcherDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
    abstract fun foodEntryDao(): FoodEntryDao

    companion object {
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
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}