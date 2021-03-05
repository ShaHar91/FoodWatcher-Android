package com.shahar91.foodwatcher.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.FoodItem

data class FoodEntryAndFoodItem(
    @Embedded val foodEntry: FoodEntry,
    @Relation(
        parentColumn = "foodItemId",
        entityColumn = "id"
    )
    val foodItem: FoodItem
) {
    fun getTotalPointValue() = (foodEntry.amount * foodItem.points).toString()
}