package com.shahar91.foodwatcher.data.models

import com.shahar91.foodwatcher.utils.CommonUtils
import java.util.*

data class FoodItem(
    override val id: String = UUID.randomUUID().toString(),
    override var name: String,
    override var description: String,
    override var points: Float,
    // 'isFavorite' will actually only be used when we retrieve a complete list of the 'FoodItems'
    override var isFavorite: Boolean = false
) : FoodItemBase(id, name, description, points, isFavorite)

open class FoodItemBase(
    open val id: String,
    open var name: String,
    open var description: String,
    open var points: Float,
    // 'isFavorite' will actually only be used when we retrieve a complete list of the 'FoodItems'
    open var isFavorite: Boolean
) {
    /**
     * In order to show the "points" in different Locale's without trailing 0's the DecimalFormat class is used.
     * This will enforce the correct pattern for that Locale
     */
    fun showPointsWithoutTrailingZero(): String {
        return CommonUtils.showValueWithoutTrailingZero(points)
    }

    /**
     * In order to edit the "points" in different Locale's, the DecimalSymbol should be a "."
     * This will make sure that the English Locale will be used and as such a "." will be enforced
     */
    fun showPointsToEdit(): String {
        return CommonUtils.showValueWithoutTrailingZero(points, Locale.ENGLISH)
    }
}