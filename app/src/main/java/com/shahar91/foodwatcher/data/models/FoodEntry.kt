package com.shahar91.foodwatcher.data.models

import com.shahar91.foodwatcher.utils.CommonUtils
import java.util.*

data class FoodEntry(
    override var id: String = UUID.randomUUID().toString(),
    override var amount: Float = 0f,
    override var date: Long = 0,
    override var meal: Meal = Meal.BREAKFAST,
    override var foodItemId: String = "",
    override var foodItemName: String = "",
    override var foodItemDescription: String = "",
    override var foodItemPoints: Float = 0f
) : FoodEntryBase(id, amount, date, meal, foodItemId, foodItemName, foodItemDescription, foodItemPoints)

open class FoodEntryBase(
    open val id: String,
    open var amount: Float,
    open var date: Long,
    open var meal: Meal,
    open var foodItemId: String,
    open var foodItemName: String,
    open var foodItemDescription: String,
    open var foodItemPoints: Float
) {

    /**
     * In order to show the "points" in different Locale's without trailing 0's the DecimalFormat class is used.
     * This will enforce the correct pattern for that Locale
     */
    fun getTotalPointValueWithoutTrailingZero(): String {
        return CommonUtils.showValueWithoutTrailingZero(amount * foodItemPoints)
    }
}