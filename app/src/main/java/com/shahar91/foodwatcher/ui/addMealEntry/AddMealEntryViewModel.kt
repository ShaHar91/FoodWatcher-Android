package com.shahar91.foodwatcher.ui.addMealEntry

import androidx.lifecycle.MutableLiveData
import be.appwise.core.extensions.viewmodel.singleArgViewModelFactory
import be.appwise.core.ui.base.BaseViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.Meal
import com.shahar91.foodwatcher.data.repository.FavoriteFoodItemRepository
import com.shahar91.foodwatcher.data.repository.FoodEntryRepository
import com.shahar91.foodwatcher.data.repository.FoodItemRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddMealEntryViewModel(private val foodItemId: Int) : BaseViewModel() {
    companion object {
        val FACTORY = singleArgViewModelFactory(::AddMealEntryViewModel)
    }

    var foodItem = FoodItemRepository.getFoodItemById(foodItemId)
    var servingSize = MutableLiveData<String>()
    var selectionAsString = MutableLiveData("")

    var selectedDateMillis = MaterialDatePicker.todayInUtcMilliseconds()
        private set

    init {
        dateFormat(selectedDateMillis)
    }

    fun dateFormat(dateSelected: Long) {
        selectedDateMillis = dateSelected
        val date = Date(dateSelected)

        val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
        selectionAsString.postValue(formattedDate)
    }

    fun saveMealEntry(servingSize: Int, date: Long, meal: Meal, onSuccess: () -> Unit) = vmScope.launch {
        FoodEntryRepository.createFoodEntry(
            FoodEntry(
                amount = servingSize,
                date = date,
                meal = meal,
                foodItemName = foodItem.value?.name ?: "",
                foodItemDescription = foodItem.value?.description ?: "",
                foodItemPoints = foodItem.value?.points ?: 0F
            )
        )
        onSuccess()
    }

    fun toggleFavoriteFoodItem() = vmScope.launch {
        foodItem.value?.let {
            if (it.isFavorite) {
                FavoriteFoodItemRepository.unFavoriteFoodItem(it.id)
            } else {
                FavoriteFoodItemRepository.favoriteFoodItem(it.id)
            }
        }
    }
}