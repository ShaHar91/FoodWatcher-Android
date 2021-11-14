package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.FoodItemDao

class FoodItemRepositoryImpl(
    foodItemDao: FoodItemDao
) : FoodItemRepository(foodItemDao)