package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.FoodItemDao
import com.shahar91.foodwatcher.data.models.FoodItem

class FoodItemRepositoryImpl(
    foodItemDao: FoodItemDao
) : FoodItemRepository(foodItemDao) {

    override suspend fun createFoodItem(foodItem: FoodItem): Long {
//        val foodItem2 = FoodItem2(foodItem.id, foodItem.name, foodItem.description, foodItem.points, foodItem.isFavorite)
//        FirebaseFirestore.getInstance().collection("foodItems").document(foodItem2.id.toString()).set(foodItem2)
        return -1L
    }
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