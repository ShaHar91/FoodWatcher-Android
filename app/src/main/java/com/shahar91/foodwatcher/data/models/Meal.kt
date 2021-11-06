package com.shahar91.foodwatcher.data.models

enum class Meal(val id: Int, val content: String) {
    BREAKFAST(0, "Breakfast"),
    LUNCH(1, "Lunch"),
    DINNER(2, "Dinner"),
    SNACK(3, "Snack");

    companion object {
        fun getMeal(id: Int): Meal {
            return values().find { it.id == id } ?: throw ClassCastException("Could not cast to correct Meal")
        }
    }
}