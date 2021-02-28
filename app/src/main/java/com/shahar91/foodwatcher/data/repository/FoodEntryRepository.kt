package com.shahar91.foodwatcher.data.repository

import be.appwise.core.data.base.BaseRepository
import com.shahar91.foodwatcher.data.database.FoodWatcherDatabase

object FoodEntryRepository : BaseRepository() {
    private val foodEntryDao = FoodWatcherDatabase.getDatabase().foodEntryDao()

    fun getFoodEntries() = foodEntryDao.getFoodEntries()
}