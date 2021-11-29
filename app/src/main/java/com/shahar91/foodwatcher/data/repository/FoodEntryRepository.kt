package com.shahar91.foodwatcher.data.repository

import androidx.lifecycle.LiveData
import com.shahar91.foodwatcher.data.models.FoodEntry
import kotlinx.coroutines.flow.Flow

interface FoodEntryRepository {

    fun getFoodEntriesFlow(fromDate: Long, toDate: Long): Flow<List<FoodEntry>>

    fun getFoodEntries(fromDate: Long, toDate: Long): LiveData<List<FoodEntry>>

    suspend fun createFoodEntry(foodEntry: FoodEntry): Long

    suspend fun deleteFoodEntry(foodEntry: FoodEntry)

    suspend fun getWeekTotal(startWeek: Long): Float

    suspend fun findFoodEntryById(foodEntryId: String): FoodEntry?

    suspend fun updateFoodEntry(foodEntry: FoodEntry)
}