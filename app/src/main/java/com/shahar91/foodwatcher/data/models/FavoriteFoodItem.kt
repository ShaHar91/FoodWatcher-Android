package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants

@Entity(tableName = DBConstants.FAVORITE_FOOD_ITEM_TABLE_NAME)
data class FavoriteFoodItem(
    @PrimaryKey override val id: String = "a",
    // TODO: Technically, this ne¬eds a column for the userID's as well, so the favorites can be saved PER USER
    var foodItemId: String
) : BaseEntity
