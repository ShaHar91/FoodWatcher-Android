package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants
import java.util.*

@Entity(tableName = DBConstants.DAY_DESCRIPTION_TABLE_NAME, indices = [Index(value = ["date"], unique = true)])
data class DayDescriptionEntity(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    override var description: String,
    override var date: Long
) : BaseEntity, DayDescription(id, description, date)

fun DayDescription.createEntity() =
    DayDescriptionEntity(this.id, this.description, this.date)