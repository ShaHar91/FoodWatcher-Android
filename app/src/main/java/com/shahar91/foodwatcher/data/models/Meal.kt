package com.shahar91.foodwatcher.data.models

enum class Meal(val id: Int, val listId: Long, val content: String) {
    BREAKFAST(0, Long.MIN_VALUE, "Breakfast"),
    LUNCH(1, Long.MIN_VALUE + 1, "Lunch"),
    DINNER(2, Long.MIN_VALUE + 2, "Dinner"),
    SNACK(3, Long.MIN_VALUE + 3, "Snack");

    companion object {
        fun getMeal(id: Int): Meal {
            return values().find { it.id == id } ?: throw ClassCastException("Could not cast to correct Meal")
        }
    }
}