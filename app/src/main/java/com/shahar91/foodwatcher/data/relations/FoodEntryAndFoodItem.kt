package com.shahar91.foodwatcher.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.FoodItem
import java.text.DecimalFormat

data class FoodEntryAndFoodItem(
    @Embedded val foodEntry: FoodEntry,
    @Relation(
        parentColumn = "foodItemId",
        entityColumn = "id"
    )
    val foodItem: FoodItem
) {
    fun getTotalPointValueWithoutTrailingZero(): String {
        return DecimalFormat("#####.#").format(foodEntry.amount * foodItem.points)
    }
}