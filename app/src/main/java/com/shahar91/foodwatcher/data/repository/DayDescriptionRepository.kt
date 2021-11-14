package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.DayDescriptionDao
import com.shahar91.foodwatcher.data.models.DayDescription

abstract class DayDescriptionRepository(
    private val dayDescriptionDao: DayDescriptionDao
) {

    suspend fun createDayDescription(dayDescription: DayDescription) = dayDescriptionDao.insert(dayDescription)

    fun getDescriptionForDay(fromDate: Long, toDate: Long) = dayDescriptionDao.getDescriptionForDay(fromDate, toDate)

    suspend fun deleteDayDescription(dayDescription: DayDescription) = dayDescriptionDao.delete(dayDescription)
}