package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.utils.CommonUtils

@Entity(tableName = DBConstants.FOOD_ENTRY_TABLE_NAME)
data class FoodEntry(
    @PrimaryKey
    override var id: String = "a",
    var amount: Float,
    var date: Long,
    var meal: Meal,
    var foodItemId: String = "a",
    var foodItemName: String,
    var foodItemDescription: String,
    var foodItemPoints: Float
) : BaseEntity {

    /**
     * In order to show the "points" in different Locale's without trailing 0's the DecimalFormat class is used.
     * This will enforce the correct pattern for that Locale
     */
    fun getTotalPointValueWithoutTrailingZero(): String {
        return CommonUtils.showValueWithoutTrailingZero(amount * foodItemPoints)
    }
}