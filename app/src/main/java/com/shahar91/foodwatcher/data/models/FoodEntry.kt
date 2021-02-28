package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.core.data.base.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants

@Entity(tableName = DBConstants.FOOD_ENTRY_TABLE_NAME)
data class FoodEntry(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    var foodItemId: Int,
    var amount: Int,
    var date: Long,
    var meal: String
) : BaseEntity()