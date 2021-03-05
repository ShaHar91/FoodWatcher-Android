package com.shahar91.foodwatcher.data.repository

import be.appwise.core.data.base.BaseRepository
import com.shahar91.foodwatcher.data.database.FoodWatcherDatabase
import com.shahar91.foodwatcher.data.models.FoodItem

object FoodItemRepository : BaseRepository() {
    private val foodItemDao = FoodWatcherDatabase.getDatabase().foodItemDao()

    fun getFoodItems() = foodItemDao.findAllLive()

    fun getFoodItemById(foodItemId: Int) = foodItemDao.findItemById(foodItemId)

    suspend fun createFoodItem(foodItem: FoodItem) = foodItemDao.insert(foodItem)

    fun getFoodItemsByQuery(query: String) = foodItemDao.findItemsByQueryLive(query)
}