package com.shahar91.foodwatcher.data.repository

import androidx.lifecycle.LiveData
import com.shahar91.foodwatcher.data.models.FoodEntry

interface FoodEntryRepository {

    fun getFoodEntries(fromDate: Long, toDate: Long): LiveData<List<FoodEntry>>

    suspend fun createFoodEntry(foodEntry: FoodEntry): Long

    suspend fun deleteFoodEntry(foodEntry: FoodEntry)

    suspend fun getWeekTotal(startWeek: Long): Float

    suspend fun findFoodEntryById(foodEntryId: Int): FoodEntry?

    suspend fun updateFoodEntry(foodEntry: FoodEntry)
}