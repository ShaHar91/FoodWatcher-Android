package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import be.appwise.core.data.base.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants

@Entity(tableName = DBConstants.DAY_DESCRIPTION_TABLE_NAME, indices = [Index(value = ["date"], unique = true)])
data class DayDescription(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    var description: String,
    var date: Long
) : BaseEntity()