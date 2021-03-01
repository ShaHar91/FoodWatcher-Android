package com.shahar91.foodwatcher.data.database

import androidx.room.TypeConverter
import com.shahar91.foodwatcher.data.models.Meal

class Converters {
    @TypeConverter
    fun fromMeal(meal: Meal): Int {
        return meal.id
    }

    @TypeConverter
    fun toMeal(mealId: Int): Meal {
        return Meal.getMeal(mealId)
    }
}