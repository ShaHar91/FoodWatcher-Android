package com.shahar91.foodwatcher.data.repository

import androidx.lifecycle.LiveData
import com.shahar91.foodwatcher.data.models.FoodItem

interface FoodItemRepository {

    fun findItemByIdWithFavoriteLive(foodItemId: String): LiveData<FoodItem>

    suspend fun findFoodItemById(foodItemId: String): FoodItem?

    suspend fun createFoodItem(foodItem: FoodItem): Long

    suspend fun deleteFoodItem(foodItem: FoodItem)

    suspend fun updateFoodItem(foodItem: FoodItem)

    fun getFoodItemsByQuery(query: String): LiveData<List<FoodItem>>
}