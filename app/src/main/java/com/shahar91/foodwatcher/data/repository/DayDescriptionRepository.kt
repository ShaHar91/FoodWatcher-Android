package com.shahar91.foodwatcher.data.repository

import be.appwise.networking.base.BaseRepository
import com.shahar91.foodwatcher.data.dao.DayDescriptionDao
import com.shahar91.foodwatcher.data.models.DayDescription

class DayDescriptionRepository(
    private val dayDescriptionDao: DayDescriptionDao
) : BaseRepository {

    suspend fun createDayDescription(dayDescription: DayDescription) = dayDescriptionDao.insert(dayDescription)

    fun getDescriptionForDay(fromDate: Long, toDate: Long) = dayDescriptionDao.getDescriptionForDay(fromDate, toDate)

    suspend fun deleteDayDescription(dayDescription: DayDescription) = dayDescriptionDao.delete(dayDescription)
}