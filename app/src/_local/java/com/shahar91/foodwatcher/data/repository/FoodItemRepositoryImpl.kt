package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.FoodItemDao
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.data.models.createEntity

class FoodItemRepositoryImpl(
    private val foodItemDao: FoodItemDao
) : FoodItemRepository {

    override fun findItemByIdWithFavoriteLive(foodItemId: String) = foodItemDao.findItemByIdWithFavoriteLive(foodItemId)

    override suspend fun findFoodItemById(foodItemId: String) = foodItemDao.findItemById(foodItemId)

    override suspend fun createFoodItem(foodItem: FoodItem) = foodItemDao.insert(foodItem.createEntity())

    override suspend fun deleteFoodItem(foodItem: FoodItem) = foodItemDao.delete(foodItem.createEntity())

    override suspend fun updateFoodItem(foodItem: FoodItem) = foodItemDao.update(foodItem.createEntity())

    override fun getFoodItemsByQuery(query: String) = foodItemDao.findItemsByQueryLive(query)
}