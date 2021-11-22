package com.shahar91.foodwatcher.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.appwise.room.BaseEntity
import com.shahar91.foodwatcher.data.DBConstants
import java.util.*

@Entity(tableName = DBConstants.FOOD_ENTRY_TABLE_NAME)
data class FoodEntryEntity(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    override var amount: Float,
    override var date: Long,
    override var meal: Meal,
    override var foodItemId: String = "",
    override var foodItemName: String,
    override var foodItemDescription: String,
    override var foodItemPoints: Float
) : BaseEntity, FoodEntryBase(id, amount, date, meal, foodItemId, foodItemName, foodItemDescription, foodItemPoints)

fun FoodEntry.createEntity() = FoodEntryEntity(id, amount, date, meal, foodItemId, foodItemName, foodItemDescription, foodItemPoints)