package com.shahar91.foodwatcher.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shahar91.foodwatcher.data.models.FoodEntry

class FoodEntryRepositoryImpl : FoodEntryRepository {
    override fun getFoodEntries(fromDate: Long, toDate: Long): LiveData<List<FoodEntry>> = MutableLiveData()

    override suspend fun createFoodEntry(foodEntry: FoodEntry): Long = -1

    override suspend fun deleteFoodEntry(foodEntry: FoodEntry) {}

    override suspend fun getWeekTotal(startWeek: Long): Float = -0f

    override suspend fun findFoodEntryById(foodEntryId: String): FoodEntry? = null

    override suspend fun updateFoodEntry(foodEntry: FoodEntry) {}
}