package com.shahar91.foodwatcher.data.repository

import com.google.firebase.firestore.FirebaseFirestore


class FavoriteFoodItemRepositoryImpl : FavoriteFoodItemRepository {

    companion object {
        private const val COLLECTION_FOOD_ITEM = "foodItems"
    }

    override suspend fun favoriteFoodItem(foodItemId: String): Long {
        FirebaseFirestore.getInstance().collection(COLLECTION_FOOD_ITEM).document(foodItemId).update("favorite", true)
        return -1L
    }

    override suspend fun unFavoriteFoodItem(foodItemId: String) {
        FirebaseFirestore.getInstance().collection(COLLECTION_FOOD_ITEM).document(foodItemId).update("favorite", false)
    }
}