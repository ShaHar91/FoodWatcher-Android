package com.shahar91.foodwatcher.data.dao

import androidx.room.Dao
import androidx.room.Query
import be.appwise.core.data.base.BaseRoomDao
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.FoodItem

@Dao
abstract class FoodItemDao: BaseRoomDao<FoodItem>(DBConstants.FOOD_ITEM_TABLE_NAME) {
    @Query("SELECT * FROM foodItem")
    abstract suspend fun findAll(): List<FoodItem>
}