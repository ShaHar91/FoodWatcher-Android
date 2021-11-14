package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.DayDescriptionDao
import com.shahar91.foodwatcher.data.models.DayDescription

class DayDescriptionRepositoryImpl(
    private val dayDescriptionDao: DayDescriptionDao
) : DayDescriptionRepository {

    override suspend fun createDayDescription(dayDescription: DayDescription) = dayDescriptionDao.insert(dayDescription)

    override fun getDescriptionForDay(fromDate: Long, toDate: Long) = dayDescriptionDao.getDescriptionForDay(fromDate, toDate)

    override suspend fun deleteDayDescription(dayDescription: DayDescription) = dayDescriptionDao.delete(dayDescription)
}