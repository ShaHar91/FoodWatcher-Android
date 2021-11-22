package com.shahar91.foodwatcher.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shahar91.foodwatcher.data.models.DayDescription


class DayDescriptionRepositoryImpl : DayDescriptionRepository {
    override suspend fun createDayDescription(dayDescription: DayDescription): Long = -1

    override fun getDescriptionForDay(fromDate: Long, toDate: Long): LiveData<DayDescription> = MutableLiveData()

    override suspend fun deleteDayDescription(dayDescription: DayDescription) {}
}