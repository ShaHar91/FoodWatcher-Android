package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.utils.CommonUtils
import java.util.*

@Entity(tableName = DBConstants.FOOD_ITEM_TABLE_NAME)
data class FoodItem(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    var name: String,
    var description: String,
    var points: Float,
    // 'isFavorite' will actually only be used when we retrieve a complete list of the 'FoodItems'
    var isFavorite: Boolean = false
) : BaseEntity {
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