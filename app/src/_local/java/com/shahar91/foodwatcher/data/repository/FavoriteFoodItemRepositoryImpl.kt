package com.shahar91.foodwatcher.data.repository

import com.shahar91.foodwatcher.data.dao.FavoriteFoodItemDao

class FavoriteFoodItemRepositoryImpl(
    favoriteFoodItemDao: FavoriteFoodItemDao
) : FavoriteFoodItemRepository(favoriteFoodItemDao)