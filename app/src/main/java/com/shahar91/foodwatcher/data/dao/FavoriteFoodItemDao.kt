package com.shahar91.foodwatcher.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import be.appwise.core.data.base.BaseRoomDao
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.FavoriteFoodItem

@Dao
abstract class FavoriteFoodItemDao : BaseRoomDao<FavoriteFoodItem>(DBConstants.FAVORITE_FOOD_ITEM_TABLE_NAME) {
    @Query("DELETE FROM ${DBConstants.FAVORITE_FOOD_ITEM_TABLE_NAME} WHERE foodItemId == :foodItemId")
    abstract suspend fun unFavoriteFoodItem(foodItemId: Int)
}