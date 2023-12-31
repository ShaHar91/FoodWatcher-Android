package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.FoodEntryDao
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.createEntity
import kotlinx.coroutines.flow.Flow

class FoodEntryRepositoryImpl(
    private val foodEntryDao: FoodEntryDao
) : FoodEntryRepository {

    override fun getFoodEntriesFlow(fromDate: Long, toDate: Long) = foodEntryDao.getFoodEntriesFlow(fromDate, toDate)

    override fun getFoodEntries(fromDate: Long, toDate: Long) = foodEntryDao.getFoodEntries(fromDate, toDate)

    override suspend fun createFoodEntry(foodEntry: FoodEntry) = foodEntryDao.insert(foodEntry.createEntity())

    override suspend fun deleteFoodEntry(foodEntry: FoodEntry) = foodEntryDao.delete(foodEntry.createEntity())

    override suspend fun getWeekTotal(startWeek: Long) = foodEntryDao.getWeekTotal(startWeek)

    override suspend fun findFoodEntryById(foodEntryId: String) = foodEntryDao.findEntryById(foodEntryId)

    override suspend fun updateFoodEntry(foodEntry: FoodEntry) = foodEntryDao.update(foodEntry.createEntity())
}