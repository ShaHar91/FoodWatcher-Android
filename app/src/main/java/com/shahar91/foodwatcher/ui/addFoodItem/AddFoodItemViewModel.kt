package com.shahar91.foodwatcher.ui.addFoodItem

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import be.appwise.core.extensions.viewmodel.singleArgViewModelFactory
import be.appwise.core.ui.base.BaseViewModel
import com.google.android.material.textfield.TextInputLayout
import com.shahar91.foodwatcher.data.DBConstants
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.data.repository.FoodItemRepository
import kotlinx.coroutines.launch
import java.util.*

class AddFoodItemViewModel(private val foodItemId: Int) : BaseViewModel() {
    enum class State {
        ADD,
        EDIT
    }

    companion object {
        val FACTORY = singleArgViewModelFactory(::AddFoodItemViewModel)
    }

    init {
        vmScope.launch {
            FoodItemRepository.findFoodItemById(foodItemId)?.let {
                this@AddFoodItemViewModel.name.postValue(it.name)
                this@AddFoodItemViewModel.description.postValue(it.description)
                this@AddFoodItemViewModel.points.postValue(it.points.toString())
            }
        }
    }

    private val state: State = when (foodItemId) {
        DBConstants.INVALID_ID -> State.ADD
        else -> State.EDIT
    }

    var name = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var points = MutableLiveData<String>()

    fun isAddingNew() = state == State.ADD

    fun saveFoodItem(name: String, description: String, points: Float, onSuccess: () -> Unit) = vmScope.launch {
        when (state) {
            State.ADD -> FoodItemRepository.createFoodItem(FoodItem(name = name, description = description, points = points))
            State.EDIT -> FoodItemRepository.updateFoodItem(FoodItem(foodItemId, name, description, points))
        }

        onSuccess()
    }
}

@BindingAdapter("android:errorText")
fun setError(tInputLayout: TextInputLayout, str: String) {
    tInputLayout.error = str
}