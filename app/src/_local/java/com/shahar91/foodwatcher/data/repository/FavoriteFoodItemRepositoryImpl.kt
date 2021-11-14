package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.FavoriteFoodItemDao
import com.shahar91.foodwatcher.data.models.FavoriteFoodItem

class FavoriteFoodItemRepositoryImpl(
    private val favoriteFoodItemDao: FavoriteFoodItemDao
) : FavoriteFoodItemRepository {

    override suspend fun favoriteFoodItem(foodItemId: Int) = favoriteFoodItemDao.insert(FavoriteFoodItem(foodItemId = foodItemId))

    override suspend fun unFavoriteFoodItem(foodItemId: Int) = favoriteFoodItemDao.unFavoriteFoodItem(foodItemId)
}