package com.shahar91.foodwatcher.data.repository

interface FavoriteFoodItemRepository {

    suspend fun favoriteFoodItem(foodItemId: Int): Long

    suspend fun unFavoriteFoodItem(foodItemId: Int)
}