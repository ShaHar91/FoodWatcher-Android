package com.shahar91.foodwatcher.data.repository

import be.appwise.networking.base.BaseRepository
import com.shahar91.foodwatcher.data.dao.FavoriteFoodItemDao
import com.shahar91.foodwatcher.data.models.FavoriteFoodItem

class FavoriteFoodItemRepository(
    private val favoriteFoodItemDao: FavoriteFoodItemDao
) : BaseRepository {

    suspend fun favoriteFoodItem(foodItemId: Int) = favoriteFoodItemDao.insert(FavoriteFoodItem(foodItemId = foodItemId))

    suspend fun unFavoriteFoodItem(foodItemId: Int) = favoriteFoodItemDao.unFavoriteFoodItem(foodItemId)
}