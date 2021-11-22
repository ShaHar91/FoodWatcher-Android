package com.shahar91.foodwatcher.data.dao

import androidx.room.Dao
import androidx.room.Query
import be.appwise.room.BaseRoomDao
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.FavoriteFoodItem
import com.shahar91.foodwatcher.data.models.FavoriteFoodItemEntity

@Dao
abstract class FavoriteFoodItemDao : BaseRoomDao<FavoriteFoodItemEntity>(DBConstants.FAVORITE_FOOD_ITEM_TABLE_NAME) {
    @Query("DELETE FROM ${DBConstants.FAVORITE_FOOD_ITEM_TABLE_NAME} WHERE foodItemId == :foodItemId")
    abstract suspend fun unFavoriteFoodItem(foodItemId: String)
}