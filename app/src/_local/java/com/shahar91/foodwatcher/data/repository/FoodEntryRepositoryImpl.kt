package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.FoodEntryDao
import com.shahar91.foodwatcher.data.models.FoodEntry

class FoodEntryRepositoryImpl(
    private val foodEntryDao: FoodEntryDao
) : FoodEntryRepository {

    override fun getFoodEntries(fromDate: Long, toDate: Long) = foodEntryDao.getFoodEntries(fromDate, toDate)

    override suspend fun createFoodEntry(foodEntry: FoodEntry) = foodEntryDao.insert(foodEntry)

    override suspend fun deleteFoodEntry(foodEntry: FoodEntry) = foodEntryDao.delete(foodEntry)

    override suspend fun getWeekTotal(startWeek: Long) = foodEntryDao.getWeekTotal(startWeek)

    override suspend fun findFoodEntryById(foodEntryId: Int) = foodEntryDao.findEntryById(foodEntryId)

    override suspend fun updateFoodEntry(foodEntry: FoodEntry) = foodEntryDao.update(foodEntry)
}