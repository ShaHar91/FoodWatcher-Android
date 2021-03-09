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

    /**
     * To format a String to a Float I have to use the DecimalFormat class.
     * Especially if I want to target multiple Locale's.
     *
     * The DecimalSymbol in English is a '.' and for most other countries it is ','.
     * A ',' does not work well with Room it seems...
     */
    fun getNumberAsFloat(points: String?): Float {
        return DecimalFormat("#####.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH)).format(points?.toFloat()).toFloat()
    }
}

@BindingAdapter("android:errorText")
fun setError(tInputLayout: TextInputLayout, str: String) {
    tInputLayout.error = str
}