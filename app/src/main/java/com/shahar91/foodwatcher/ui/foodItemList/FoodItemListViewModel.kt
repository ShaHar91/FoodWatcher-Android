package com.shahar91.foodwatcher.ui.foodItemList

import androidx.lifecycle.LiveData
import be.appwise.core.ui.base.BaseViewModel
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.data.repository.FoodItemRepository

class FoodItemListViewModel : BaseViewModel() {
    val items :LiveData<List<FoodItem>> = FoodItemRepository.getFoodItems()
}