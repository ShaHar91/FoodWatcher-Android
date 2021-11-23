package com.shahar91.foodwatcher.ui.addMealEntry

import androidx.core.view.children
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.appwise.core.ui.base.BaseViewModel
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.Meal
import com.shahar91.foodwatcher.data.repository.FavoriteFoodItemRepository
import com.shahar91.foodwatcher.data.repository.FoodEntryRepository
import com.shahar91.foodwatcher.data.repository.FoodItemRepository
import com.shahar91.foodwatcher.utils.CommonUtils
import com.shahar91.foodwatcher.utils.Constants
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddMealEntryViewModel(
    private val foodItemId: String,
    private val foodEntryId: String,
    private val foodEntryRepository: FoodEntryRepository,
    private val foodItemRepository: FoodItemRepository,
    private val favoriteFoodItemRepository: FavoriteFoodItemRepository
) : BaseViewModel() {

    enum class State {
        ADD,
        EDIT
    }

    init {
        vmScope.launch {
            foodEntryRepository.findFoodEntryById(foodEntryId)?.let {
                this@AddMealEntryViewModel.foodEntry = it
                this@AddMealEntryViewModel.servingSize.postValue(CommonUtils.showValueWithoutTrailingZero(it.amount))
                this@AddMealEntryViewModel.mealChecked.postValue(it.meal.id)
                this@AddMealEntryViewModel.setSelectedDateMillis(it.date)
            }
        }
    }

    lateinit var foodEntry: FoodEntry
    val foodItem = foodItemRepository.findItemByIdWithFavoriteLive(foodItemId)
    var servingSize = MutableLiveData<String>()

    var mealChecked = MutableLiveData(Meal.BREAKFAST.id)

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
        Constants.INVALID_ID -> State.ADD
        else -> State.EDIT
    }

    fun isAddingNew() = state == State.ADD

    fun saveMealEntry(servingSize: Float, onSuccess: () -> Unit) = vmScope.launch {
        when (state) {
            State.ADD -> {
                foodEntryRepository.createFoodEntry(
                    FoodEntry(
                        amount = servingSize,
                        date = selectedDateMillis.value ?: 0,
                        meal = Meal.getMeal(mealChecked.value ?: 0),
                        foodItemId = foodItemId,
                        foodItemName = foodItem.value?.name ?: "",
                        foodItemDescription = foodItem.value?.description ?: "",
                        foodItemPoints = foodItem.value?.points ?: 0F
                    )
                )
            }
            State.EDIT -> {
                foodEntryRepository.updateFoodEntry(foodEntry.apply {
                    this.amount = servingSize
                    this.date = selectedDateMillis.value ?: 0
                    this.meal = Meal.getMeal(mealChecked.value ?: 0)
                })
            }
        }

        onSuccess()
    }

    fun toggleFavoriteFoodItem() = vmScope.launch {
        foodItem.value?.let {
            if (it.isFavorite) {
                favoriteFoodItemRepository.unFavoriteFoodItem(it.id)
            } else {
                favoriteFoodItemRepository.favoriteFoodItem(it.id)
            }
        }
    }

    fun deleteFoodEntry(foodEntry: FoodEntry, onSuccess: () -> Unit) = vmScope.launch {
        foodEntryRepository.deleteFoodEntry(foodEntry)
        onSuccess()
    }

    fun deleteFoodItem(onSuccess: () -> Unit) = vmScope.launch {
        foodItem.value?.let {
            foodItemRepository.deleteFoodItem(it)
            onSuccess()
        }
    }
}

@BindingAdapter("checkedBtnAttrChanged")
fun setToggleGroupChangedListener(toggleGroup: MaterialButtonToggleGroup, listener: InverseBindingListener) {
    toggleGroup.addOnButtonCheckedListener { _, _, _ -> listener.onChange() }
}

@BindingAdapter("checkedBtn")
fun setChecked(toggleGroup: MaterialButtonToggleGroup, ordinal: Int) {
    toggleGroup.check(toggleGroup.getChildAt(ordinal).id)
}

@InverseBindingAdapter(attribute = "checkedBtn")
fun getChecked(toggleGroup: MaterialButtonToggleGroup): Int {
    val checkedId = toggleGroup.checkedButtonId
    toggleGroup.children.forEachIndexed { index, view ->
        if (view.id == checkedId) {
            return index
        }
    }
    return -1
}