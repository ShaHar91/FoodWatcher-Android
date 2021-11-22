package com.shahar91.foodwatcher.data.models

import java.util.*

open class DayDescription(
    open val id: String = UUID.randomUUID().toString(),
    open var description: String,
    open var date: Long
)