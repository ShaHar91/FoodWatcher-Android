package com.shahar91.foodwatcher.data.models

import java.util.*

open class FavoriteFoodItem(
    open val id: String = UUID.randomUUID().toString(),
    // TODO: Technically, this ne¬eds a column for the userID's as well, so the favorites can be saved PER USER
    open var foodItemId: String
)
