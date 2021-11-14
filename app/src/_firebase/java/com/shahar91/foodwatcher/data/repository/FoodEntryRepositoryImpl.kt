package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.FoodEntryDao

class FoodEntryRepositoryImpl(
    foodEntryDao: FoodEntryDao
) : FoodEntryRepository(foodEntryDao)