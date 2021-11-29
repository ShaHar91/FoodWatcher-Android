package com.shahar91.foodwatcher.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import be.appwise.room.BaseRoomDao
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.DayDescription
import com.shahar91.foodwatcher.data.models.DayDescriptionEntity

@Dao
abstract class DayDescriptionDao : BaseRoomDao<DayDescriptionEntity>(DBConstants.DAY_DESCRIPTION_TABLE_NAME) {
    @Query("SELECT * FROM ${DBConstants.DAY_DESCRIPTION_TABLE_NAME} WHERE date BETWEEN :fromDate AND :toDate")
    abstract fun getDescriptionForDay(fromDate: Long, toDate: Long): LiveData<DayDescription?>
}