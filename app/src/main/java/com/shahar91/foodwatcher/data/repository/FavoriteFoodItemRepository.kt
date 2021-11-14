package com.shahar91.foodwatcher.data.repository

interface FavoriteFoodItemRepository {

    suspend fun favoriteFoodItem(foodItemId: String): Long

    suspend fun unFavoriteFoodItem(foodItemId: String)
}