package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.DayDescriptionDao

class DayDescriptionRepositoryImpl(
    dayDescriptionDao: DayDescriptionDao
) : DayDescriptionRepository(dayDescriptionDao)