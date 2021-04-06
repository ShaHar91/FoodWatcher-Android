package com.shahar91.foodwatcher.data.repository

import be.appwise.core.data.base.BaseRepository
import com.shahar91.foodwatcher.data.database.FoodWatcherDatabase
import com.shahar91.foodwatcher.data.models.DayDescription

object DayDescriptionRepository : BaseRepository() {
    private val dayDescriptionDao = FoodWatcherDatabase.getDatabase().dayDescriptionDao()

    suspend fun createDayDescription(dayDescription: DayDescription) = dayDescriptionDao.insert(dayDescription)

    fun getDescriptionForDay(fromDate: Long, toDate: Long) = dayDescriptionDao.getDescriptionForDay(fromDate, toDate)

    suspend fun deleteDayDescription(dayDescription: DayDescription) = dayDescriptionDao.delete(dayDescription)
}