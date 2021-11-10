package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.utils.CommonUtils

@Entity(tableName = DBConstants.FOOD_ENTRY_TABLE_NAME)
data class FoodEntry(
    @Ignore
    override var id: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var someId: Int = 0,
    var amount: Float,
    var date: Long,
    var meal: Meal,
    var foodItemId: Int = -1,
    var foodItemName: String,
    var foodItemDescription: String,
    var foodItemPoints: Float
) : BaseEntity {

    constructor(amount: Float, date: Long, meal: Meal, foodItemId: Int, foodItemName: String, foodItemDescription: String, foodItemPoints: Float)
            : this(0, 0, amount, date, meal, foodItemId, foodItemName, foodItemDescription, foodItemPoints)

    /**
     * In order to show the "points" in different Locale's without trailing 0's the DecimalFormat class is used.
     * This will enforce the correct pattern for that Locale
     */
    fun getTotalPointValueWithoutTrailingZero(): String {
        return CommonUtils.showValueWithoutTrailingZero(amount * foodItemPoints)
    }
}