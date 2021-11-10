package com.shahar91.foodwatcher.data.models

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.shahar91.foodwatcher.R

enum class Meal(val id: Int, val content: String, @DrawableRes val icon: Int, @StringRes val emptyText: Int) {
    BREAKFAST(0, "Breakfast", R.drawable.ic_meal_breakfast, R.string.meal_empty_breakfast),
    LUNCH(1, "Lunch", R.drawable.ic_meal_lunch, R.string.meal_empty_lunch),
    DINNER(2, "Dinner", R.drawable.ic_meal_dinner, R.string.meal_empty_dinner),
    SNACK(3, "Snack", R.drawable.ic_meal_snack, R.string.meal_empty_snack);

    companion object {
        fun getMeal(id: Int): Meal {
            return values().find { it.id == id } ?: throw ClassCastException("Could not cast to correct Meal")
        }
    }

    fun getDrawable(context: Context) = ContextCompat.getDrawable(context, icon)
}