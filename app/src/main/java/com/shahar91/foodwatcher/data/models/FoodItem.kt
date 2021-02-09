package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.core.data.base.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants

@Entity(tableName = DBConstants.FOOD_ITEM_TABLE_NAME)
data class FoodItem(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    var name: String,
    var description: String,
    var points: Float
) : BaseEntity()