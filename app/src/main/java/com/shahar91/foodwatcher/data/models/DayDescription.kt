package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants
import java.util.*

@Entity(tableName = DBConstants.DAY_DESCRIPTION_TABLE_NAME, indices = [Index(value = ["date"], unique = true)])
data class DayDescription(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    var description: String,
    var date: Long
) : BaseEntity