package com.shahar91.foodwatcher.ui.foodItemList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.appwise.core.ui.base.BaseViewModel
import com.shahar91.foodwatcher.data.repository.FoodItemRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FoodItemListViewModel : BaseViewModel() {
    private var searchJob: Job? = null
    private val _searchQuery = MutableLiveData<String>().apply { value = "" }
    fun setQuery(query: String) {
        searchJob?.cancel()
        searchJob = vmScope.launch {
            // A 'makeshift' debounce as Coroutines does not have one natively
            delay(300)
            _searchQuery.postValue(query)
        }
    }

    val foodItems = Transformations.switchMap(_searchQuery) { FoodItemRepository.getFoodItemsByQuery(it) }
}