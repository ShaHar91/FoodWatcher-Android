package com.shahar91.foodwatcher.data.models

enum class Meal(val id: Int) {
    BREAKFAST(0),
    LUNCH(1),
    DINNER(2),
    SNACK(3);

    fun getMeal(id: Int): Meal? {
        return values().find { it.id == id }
    }
}