package com.shahar91.foodwatcher.ui.addMealEntry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.appwise.core.extensions.viewmodel.doubleArgsViewModelFactory
import be.appwise.core.ui.base.BaseViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.Meal
import com.shahar91.foodwatcher.data.repository.FavoriteFoodItemRepository
import com.shahar91.foodwatcher.data.repository.FoodEntryRepository
import com.shahar91.foodwatcher.data.repository.FoodItemRepository
import com.shahar91.foodwatcher.utils.CommonUtils
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddMealEntryViewModel(private val foodItemId: Int, private val foodEntryId: Int) : BaseViewModel() {
    enum class State {
        ADD,
        EDIT
    }

    companion object {
        val FACTORY = doubleArgsViewModelFactory(::AddMealEntryViewModel)
    }

    init {
        vmScope.launch {
            FoodEntryRepository.findFoodEntryById(foodEntryId)?.let {
                this@AddMealEntryViewModel.foodEntry = it
                this@AddMealEntryViewModel.servingSize.postValue(CommonUtils.showValueWithoutTrailingZero(it.amount))
                this@AddMealEntryViewModel.setSelectedDateMillis(it.date)
            }
        }
    }
    lateinit var foodEntry: FoodEntry
    var foodItem = FoodItemRepository.findItemByIdWithFavoriteLive(foodItemId)
    var servingSize = MutableLiveData<String>()

    private var _selectedDateMillis: MutableLiveData<Long> = MutableLiveData(MaterialDatePicker.todayInUtcMilliseconds())
    val selectedDateMillis: LiveData<Long> get() = _selectedDateMillis
    fun setSelectedDateMillis(millis: Long) {
        _selectedDateMillis.postValue(millis)
    }

    val selectionAsString = Transformations.switchMap(_selectedDateMillis) {
        MutableLiveData(dateFormat(it))
    }

    private fun dateFormat(dateSelected: Long): String {
        return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(dateSelected))
    }

    private val state: State = when (foodEntryId) {
        DBConstants.INVALID_ID -> State.ADD
        else -> State.EDIT
    }

    fun isAddingNew() = state == State.ADD

    fun saveMealEntry(servingSize: Float, date: Long, meal: Meal, onSuccess: () -> Unit) = vmScope.launch {
        when (state) {
            State.ADD -> {
                FoodEntryRepository.createFoodEntry(
                    FoodEntry(
                        amount = servingSize,
                        date = date,
                        meal = meal,
                        foodItemId = foodItemId,
                        foodItemName = foodItem.value?.name ?: "",
                        foodItemDescription = foodItem.value?.description ?: "",
                        foodItemPoints = foodItem.value?.points ?: 0F
                    )
                )
            }
            State.EDIT -> {
                FoodEntryRepository.updateFoodEntry(foodEntry.apply {
                    this.amount = servingSize
                    this.date = date
                    this.meal = meal
                })
            }
        }

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

    fun deleteFoodEntry(foodEntry: FoodEntry, onSuccess: () -> Unit) = vmScope.launch {
        FoodEntryRepository.deleteFoodEntry(foodEntry)
        onSuccess()
    }

    fun deleteFoodItem(onSuccess: () -> Unit) = vmScope.launch {
        foodItem.value?.let {
            FoodItemRepository.deleteFoodItem(it)
            onSuccess()
        }
    }
}