package com.shahar91.foodwatcher.di.modules

import com.shahar91.foodwatcher.data.repository.DayDescriptionRepository
import com.shahar91.foodwatcher.data.repository.FavoriteFoodItemRepository
import com.shahar91.foodwatcher.data.repository.FoodEntryRepository
import com.shahar91.foodwatcher.data.repository.FoodItemRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { DayDescriptionRepository(get()) }
    single { FavoriteFoodItemRepository(get()) }
    single { FoodEntryRepository(get()) }
    single { FoodItemRepository(get()) }
}