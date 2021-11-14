package com.shahar91.foodwatcher.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shahar91.foodwatcher.data.DBConstants

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `${DBConstants.DAY_DESCRIPTION_TABLE_NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `description` TEXT NOT NULL, `date` INTEGER NOT NULL)")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_dayDescription_date` ON `${DBConstants.DAY_DESCRIPTION_TABLE_NAME}` (`date`)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `${DBConstants.FOOD_ENTRY_TABLE_NAME}` ADD COLUMN foodItemId INTEGER NOT NULL DEFAULT -1")
    }
}