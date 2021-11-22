package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants
import java.util.*

@Entity(tableName = DBConstants.FAVORITE_FOOD_ITEM_TABLE_NAME)
data class FavoriteFoodItemEntity(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    // TODO: Technically, this ne¬eds a column for the userID's as well, so the favorites can be saved PER USER
    override var foodItemId: String
) : BaseEntity, FavoriteFoodItem(id, foodItemId)

fun FavoriteFoodItem.createEntity() = FavoriteFoodItemEntity(this.id, this.foodItemId)