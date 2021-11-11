package com.shahar91.foodwatcher.di.modules

import com.shahar91.foodwatcher.ui.addFoodItem.AddFoodItemViewModel
import com.shahar91.foodwatcher.ui.addMealEntry.AddMealEntryViewModel
import com.shahar91.foodwatcher.ui.foodItemList.FoodItemListViewModel
import com.shahar91.foodwatcher.ui.myDay.MyDayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { AddFoodItemViewModel(get(), get()) }
    viewModel { (foodItemId: Int, foodEntryId: Int) -> AddMealEntryViewModel(foodItemId, foodEntryId, get(), get(), get()) }
    viewModel { FoodItemListViewModel(get()) }
    viewModel { MyDayViewModel(get(), get()) }
}