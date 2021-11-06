package com.shahar91.foodwatcher.data.repository

import be.appwise.networking.base.BaseRepository
import com.shahar91.foodwatcher.data.database.FoodWatcherDatabase
import com.shahar91.foodwatcher.data.models.FavoriteFoodItem

object FavoriteFoodItemRepository : BaseRepository {
    private val favoriteFoodItemDao = FoodWatcherDatabase.getDatabase().favoriteFoodItemDao()

    suspend fun favoriteFoodItem(foodItemId: Int) = favoriteFoodItemDao.insert(FavoriteFoodItem(foodItemId = foodItemId))

    suspend fun unFavoriteFoodItem(foodItemId: Int) = favoriteFoodItemDao.unFavoriteFoodItem(foodItemId)
}