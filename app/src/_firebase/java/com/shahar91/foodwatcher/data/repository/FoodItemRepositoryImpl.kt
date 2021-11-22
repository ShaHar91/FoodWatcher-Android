package com.shahar91.foodwatcher.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.shahar91.foodwatcher.data.models.FoodItem

class FoodItemRepositoryImpl : FoodItemRepository {

    override fun findItemByIdWithFavoriteLive(foodItemId: String): LiveData<FoodItem> = MutableLiveData()

    override suspend fun findFoodItemById(foodItemId: String): FoodItem? = null

    override suspend fun createFoodItem(foodItem: FoodItem): Long {
//        val foodItem2 = FoodItem2(foodItem.id, foodItem.name, foodItem.description, foodItem.points, foodItem.isFavorite)
        FirebaseFirestore.getInstance().collection("foodItems").document(foodItem.id).set(foodItem)
        return -1L
    }

    override suspend fun deleteFoodItem(foodItem: FoodItem) {

    }

    override suspend fun updateFoodItem(foodItem: FoodItem) {

    }

    override fun getFoodItemsByQuery(query: String): LiveData<List<FoodItem>> = MutableLiveData()
}
//
//class FoodItem2(
//    val id: Int,
//    var name: String,
//    var description: String,
//    var points: Float,
//    // 'isFavorite' will actually only be used when we retrieve a complete list of the 'FoodItems'
//    @JvmField // This will make sure the field in Firebase will still be `isFavorite`
//    var isFavorite: Boolean = false
//)