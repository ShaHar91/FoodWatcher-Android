package com.shahar91.foodwatcher.di.modules

import com.shahar91.foodwatcher.data.database.FoodWatcherDatabase
import org.koin.dsl.module

val daoModule = module {

    single { get<FoodWatcherDatabase>().dayDescriptionDao() }
    single { get<FoodWatcherDatabase>().favoriteFoodItemDao() }
    single { get<FoodWatcherDatabase>().foodEntryDao() }
    single { get<FoodWatcherDatabase>().foodItemDao() }
}