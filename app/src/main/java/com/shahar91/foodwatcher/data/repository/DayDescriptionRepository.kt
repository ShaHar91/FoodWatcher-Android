package com.shahar91.foodwatcher.data.repository

import androidx.lifecycle.LiveData
import com.shahar91.foodwatcher.data.models.DayDescription

interface DayDescriptionRepository {

    suspend fun createDayDescription(dayDescription: DayDescription): Long

    fun getDescriptionForDay(fromDate: Long, toDate: Long): LiveData<DayDescription>

    suspend fun deleteDayDescription(dayDescription: DayDescription)
}