package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.core.data.base.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants
import java.text.DecimalFormat

@Entity(tableName = DBConstants.FOOD_ENTRY_TABLE_NAME)
data class FoodEntry(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    var amount: Int,
    var date: Long,
    var meal: Meal,
    var foodItemName: String,
    var foodItemDescription: String,
    var foodItemPoints: Float
) : BaseEntity() {
    fun getTotalPointValueWithoutTrailingZero(): String {
        return DecimalFormat("#####.#").format(amount * foodItemPoints)
    }
}