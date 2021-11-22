package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.FavoriteFoodItemDao
import com.shahar91.foodwatcher.data.models.FavoriteFoodItem
import com.shahar91.foodwatcher.data.models.createEntity

class FavoriteFoodItemRepositoryImpl(
    private val favoriteFoodItemDao: FavoriteFoodItemDao
) : FavoriteFoodItemRepository {

    override suspend fun favoriteFoodItem(foodItemId: String) = favoriteFoodItemDao.insert(FavoriteFoodItem(foodItemId = foodItemId).createEntity())

    override suspend fun unFavoriteFoodItem(foodItemId: String) = favoriteFoodItemDao.unFavoriteFoodItem(foodItemId)
}