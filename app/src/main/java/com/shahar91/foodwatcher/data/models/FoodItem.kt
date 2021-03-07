package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.core.data.base.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants
import java.text.DecimalFormat

@Entity(tableName = DBConstants.FOOD_ITEM_TABLE_NAME)
data class FoodItem(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    var name: String,
    var description: String,
    var points: Float,
    // 'isFavorite' will actually only be used when we retrieve a complete list of the 'FoodItems'
    var isFavorite: Boolean = false
) : BaseEntity() {
    fun showPointsWithoutTrailingZero(): String {
        return DecimalFormat("#####.#").format(points)
    }
}