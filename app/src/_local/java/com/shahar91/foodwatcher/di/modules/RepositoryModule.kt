package com.shahar91.foodwatcher.di.modules

import com.shahar91.foodwatcher.data.database.FoodWatcherDatabase
import com.shahar91.foodwatcher.data.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single { FoodWatcherDatabase.getDatabase(androidContext()) }

    single<DayDescriptionRepository> { DayDescriptionRepositoryImpl(get()) }
    single<FavoriteFoodItemRepository> { FavoriteFoodItemRepositoryImpl(get()) }
    single<FoodEntryRepository> { FoodEntryRepositoryImpl(get()) }
    single<FoodItemRepository> { FoodItemRepositoryImpl(get()) }
}