package com.shahar91.foodwatcher.data.repository

import be.appwise.core.data.base.BaseRepository
import com.shahar91.foodwatcher.data.database.FoodWatcherDatabase
import com.shahar91.foodwatcher.data.models.FoodEntry

object FoodEntryRepository : BaseRepository() {
    private val foodEntryDao = FoodWatcherDatabase.getDatabase().foodEntryDao()

    fun getFoodEntries() = foodEntryDao.getFoodEntries()

    suspend fun createFoodEntry(foodEntry: FoodEntry) = foodEntryDao.insert(foodEntry)
}