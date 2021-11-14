package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.FoodEntryDao
import com.shahar91.foodwatcher.data.models.FoodEntry

abstract class FoodEntryRepository(
    private val foodEntryDao: FoodEntryDao
) {

    fun getFoodEntries(fromDate: Long, toDate: Long) = foodEntryDao.getFoodEntries(fromDate, toDate)

    suspend fun createFoodEntry(foodEntry: FoodEntry) = foodEntryDao.insert(foodEntry)

    suspend fun deleteFoodEntry(foodEntry: FoodEntry) = foodEntryDao.delete(foodEntry)

    suspend fun getWeekTotal(startWeek: Long) = foodEntryDao.getWeekTotal(startWeek)

    suspend fun findFoodEntryById(foodEntryId: Int) = foodEntryDao.findEntryById(foodEntryId)

    suspend fun updateFoodEntry(foodEntry: FoodEntry) = foodEntryDao.update(foodEntry)
}