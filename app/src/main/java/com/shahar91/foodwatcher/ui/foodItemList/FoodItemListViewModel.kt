package com.shahar91.foodwatcher.ui.foodItemList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.appwise.core.ui.base.BaseViewModel
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.data.models.Meal
import com.shahar91.foodwatcher.data.repository.FoodEntryRepository
import com.shahar91.foodwatcher.data.repository.FoodItemRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class FoodItemListViewModel : BaseViewModel() {
    private val _searchQuery = MutableLiveData<String>().apply { value = "" }
    fun setQuery(query: String) = _searchQuery.postValue(query)
    val foodItems = Transformations.switchMap(_searchQuery) { FoodItemRepository.getFoodItemsByQuery(it) }

    fun saveFoodEntry(foodItem: FoodItem) = vmScope.launch {
        // See https://www.concretepage.com/java/java-8/convert-between-java-localdate-epoch for more information
        val date = LocalDate.now().atTime(LocalTime.NOON).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        // TODO: don't save the items here, show a new Fragment to select the "meal", "amount" and "date"
        FoodEntryRepository.createFoodEntry(FoodEntry(foodItemId = foodItem.id, amount = 1, date = date, meal = Meal.BREAKFAST))
    }
}