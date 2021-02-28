package com.shahar91.foodwatcher.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import be.appwise.core.data.base.BaseRoomDao
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.relations.FoodEntryAndFoodItem

@Dao
abstract class FoodEntryDao : BaseRoomDao<FoodEntry>(DBConstants.FOOD_ENTRY_TABLE_NAME) {
    @Query("SELECT * FROM ${DBConstants.FOOD_ENTRY_TABLE_NAME}")
    abstract fun findAllLive(): LiveData<List<FoodEntry>>

    @Transaction
    @Query("SELECT * FROM ${DBConstants.FOOD_ENTRY_TABLE_NAME}")
    abstract fun getFoodEntries(): LiveData<List<FoodEntryAndFoodItem>>
}