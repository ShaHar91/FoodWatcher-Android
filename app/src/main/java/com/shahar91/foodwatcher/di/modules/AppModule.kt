package com.shahar91.foodwatcher.di.modules

import com.shahar91.foodwatcher.data.database.FoodWatcherDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { FoodWatcherDatabase.getDatabase(androidContext()) }
}