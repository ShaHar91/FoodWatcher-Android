package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants
import java.util.*

@Entity(tableName = DBConstants.FOOD_ITEM_TABLE_NAME)
data class FoodItemEntity(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    override var name: String,
    override var description: String,
    override var points: Float,
    // 'isFavorite' will actually only be used when we retrieve a complete list of the 'FoodItems'
    override var isFavorite: Boolean = false
) : BaseEntity, FoodItemBase(id, name, description, points, isFavorite)

fun FoodItem.createEntity() = FoodItemEntity(id, name, description, points, isFavorite)