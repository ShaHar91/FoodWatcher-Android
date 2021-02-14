package com.shahar91.foodwatcher.ui.foodItemList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.appwise.core.ui.base.BaseViewModel
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.data.repository.FoodItemRepository

class FoodItemListViewModel : BaseViewModel() {
    private val _searchQuery = MutableLiveData<String>().apply { value = "" }

    fun setQuery(query: String) = _searchQuery.postValue(query)

    val foodItems = Transformations.switchMap(_searchQuery) {FoodItemRepository.getFoodItemsByQuery(it)}
}