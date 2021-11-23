package com.shahar91.foodwatcher.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.shahar91.foodwatcher.BuildConfig
import com.shahar91.foodwatcher.data.firebaseLiveData.livedata
import com.shahar91.foodwatcher.data.models.FoodItem
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FoodItemRepositoryImpl : FoodItemRepository {

    companion object {
        private const val COLLECTION_FOOD_ITEM = "foodItems"
        private const val ENVIRONMENT = BuildConfig.FLAVOR_env
    }

    override fun findItemByIdWithFavoriteLive(foodItemId: String): LiveData<FoodItem> =
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ITEM).document(foodItemId).livedata(FoodItem::class.java)

    override suspend fun findFoodItemById(foodItemId: String): FoodItem? = suspendCancellableCoroutine { c ->
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ITEM).document(foodItemId).get().addOnCompleteListener {
            if (!c.isActive) return@addOnCompleteListener

            if (it.isSuccessful) {
                c.resume(it.result?.toObject(FoodItem::class.java))
            } else {
                c.resume(null)
            }
        }
    }

    override suspend fun createFoodItem(foodItem: FoodItem): Long {
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ITEM).document(foodItem.id).set(foodItem)
        return -1L
    }

    override suspend fun deleteFoodItem(foodItem: FoodItem) {
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ITEM).document(foodItem.id).delete()
    }

    override suspend fun updateFoodItem(foodItem: FoodItem) {
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ITEM).document(foodItem.id).set(foodItem)
    }

    override fun getFoodItemsByQuery(query: String): LiveData<List<FoodItem>> =
        FirebaseFirestore.getInstance().collection(ENVIRONMENT).document(ENVIRONMENT).collection(COLLECTION_FOOD_ITEM).livedata(FoodItem::class.java).asFlow()
            .map {
                it.filter { item -> item.name.contains(query, true) }
            }
            .asLiveData()
}
