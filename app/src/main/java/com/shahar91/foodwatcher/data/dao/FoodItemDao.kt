package com.shahar91.foodwatcher.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import be.appwise.core.data.base.BaseRoomDao
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.FoodItem

@Dao
abstract class FoodItemDao : BaseRoomDao<FoodItem>(DBConstants.FOOD_ITEM_TABLE_NAME) {
    @Query("SELECT * FROM ${DBConstants.FOOD_ITEM_TABLE_NAME} ORDER BY name")
    abstract fun findAllLive(): LiveData<List<FoodItem>>

    // '||' is string concatenation for SQL queries, think of '+' as in Java
    @Query("SELECT * FROM ${DBConstants.FOOD_ITEM_TABLE_NAME} WHERE name LIKE '%'|| :query || '%' ORDER BY name")
    abstract fun findItemsByQueryLive(query: String): LiveData<List<FoodItem>>

    @Query("SELECT * FROM ${DBConstants.FOOD_ITEM_TABLE_NAME} WHERE id == :foodItemId")
    abstract fun findItemById(foodItemId: Int) : LiveData<FoodItem>
}