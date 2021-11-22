package com.shahar91.foodwatcher.di.modules

import com.shahar91.foodwatcher.data.repository.*
import org.koin.dsl.module

val repositoryModule = module {

    single<DayDescriptionRepository> { DayDescriptionRepositoryImpl() }
    single<FavoriteFoodItemRepository> { FavoriteFoodItemRepositoryImpl() }
    single<FoodEntryRepository> { FoodEntryRepositoryImpl() }
    single<FoodItemRepository> { FoodItemRepositoryImpl() }
}