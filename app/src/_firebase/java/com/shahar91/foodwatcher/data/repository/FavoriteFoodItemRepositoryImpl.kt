package com.shahar91.foodwatcher.data.repository


class FavoriteFoodItemRepositoryImpl : FavoriteFoodItemRepository {
    override suspend fun favoriteFoodItem(foodItemId: String): Long = -1

    override suspend fun unFavoriteFoodItem(foodItemId: String) {    }
}