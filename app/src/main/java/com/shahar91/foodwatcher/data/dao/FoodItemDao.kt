package com.shahar91.foodwatcher.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import be.appwise.core.data.base.BaseRoomDao
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.FoodItem

@Dao
abstract class FoodItemDao : BaseRoomDao<FoodItem>(DBConstants.FOOD_ITEM_TABLE_NAME) {
    // '||' is string concatenation for SQL queries, think of '+' as in Java
    @Query("SELECT a.id, a.name, a.description, a.points, CASE WHEN c.foodItemId IS NULL THEN 0 ELSE 1 END AS isFavorite FROM ${DBConstants.FOOD_ITEM_TABLE_NAME} a LEFT JOIN ${DBConstants.FAVORITE_FOOD_ITEM_TABLE_NAME} c ON c.foodItemId = a.id WHERE a.name LIKE '%'|| :query || '%' ORDER BY isFavorite DESC, a.name")
    abstract fun findItemsByQueryLive(query: String): LiveData<List<FoodItem>>

    @Query("SELECT a.id, a.name, a.description, a.points, CASE WHEN c.foodItemId IS NULL THEN 0 ELSE 1 END AS isFavorite FROM ${DBConstants.FOOD_ITEM_TABLE_NAME} a LEFT JOIN ${DBConstants.FAVORITE_FOOD_ITEM_TABLE_NAME} c ON c.foodItemId = a.id WHERE a.id == :foodItemId")
    abstract fun findItemById(foodItemId: Int): LiveData<FoodItem>

    @Query("DELETE FROM ${DBConstants.FOOD_ITEM_TABLE_NAME}")
    abstract suspend fun deleteAll()
}