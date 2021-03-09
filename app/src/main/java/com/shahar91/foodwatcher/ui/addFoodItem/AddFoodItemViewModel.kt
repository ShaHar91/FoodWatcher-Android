package com.shahar91.foodwatcher.ui.addFoodItem

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import be.appwise.core.ui.base.BaseViewModel
import com.google.android.material.textfield.TextInputLayout
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.data.repository.FoodItemRepository
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class AddFoodItemViewModel : BaseViewModel() {
    var name = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var points = MutableLiveData<String>()

    fun saveFoodItem(name: String, description: String, points: Float, onSuccess: () -> Unit) = vmScope.launch {
        FoodItemRepository.createFoodItem(FoodItem(name = name, description = description, points = points))
        onSuccess()
    }
}

@BindingAdapter("android:errorText")
fun setError(tInputLayout: TextInputLayout, str: String) {
    tInputLayout.error = str
}