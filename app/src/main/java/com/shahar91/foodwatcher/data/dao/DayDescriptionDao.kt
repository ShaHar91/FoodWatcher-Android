package com.shahar91.foodwatcher.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import be.appwise.core.data.base.BaseRoomDao
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.DayDescription

@Dao
abstract class DayDescriptionDao : BaseRoomDao<DayDescription>(DBConstants.DAY_DESCRIPTION_TABLE_NAME) {
    @Query("SELECT * FROM ${DBConstants.DAY_DESCRIPTION_TABLE_NAME} WHERE date BETWEEN :fromDate AND :toDate")
    abstract fun getDescriptionForDay(fromDate: Long, toDate: Long): LiveData<DayDescription>
}