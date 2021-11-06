package com.shahar91.foodwatcher.data.repository

import be.appwise.networking.base.BaseRepository
import com.shahar91.foodwatcher.data.database.FoodWatcherDatabase
import com.shahar91.foodwatcher.data.models.FoodItem

object FoodItemRepository : BaseRepository {
    private val foodItemDao = FoodWatcherDatabase.getDatabase().foodItemDao()

    fun findItemByIdWithFavoriteLive(foodItemId: Int) = foodItemDao.findItemByIdWithFavoriteLive(foodItemId)

    suspend fun findFoodItemById(foodItemId: Int) = foodItemDao.findItemById(foodItemId)

    suspend fun createFoodItem(foodItem: FoodItem) = foodItemDao.insert(foodItem)

    suspend fun deleteFoodItem(foodItem: FoodItem) = foodItemDao.delete(foodItem)

    suspend fun updateFoodItem(foodItem: FoodItem) = foodItemDao.update(foodItem)

    fun getFoodItemsByQuery(query: String) = foodItemDao.findItemsByQueryLive(query)
}