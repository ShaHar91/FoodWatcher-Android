package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.DayDescriptionDao
import com.shahar91.foodwatcher.data.models.DayDescription
import com.shahar91.foodwatcher.data.models.createEntity

class DayDescriptionRepositoryImpl(
    private val dayDescriptionDao: DayDescriptionDao
) : DayDescriptionRepository {

    override suspend fun createDayDescription(dayDescription: DayDescription) = dayDescriptionDao.insert(dayDescription.createEntity())

    override fun getDescriptionForDay(fromDate: Long, toDate: Long) = dayDescriptionDao.getDescriptionForDay(fromDate, toDate)

    override suspend fun deleteDayDescription(dayDescription: DayDescription) = dayDescriptionDao.delete(dayDescription.createEntity())
}