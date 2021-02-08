package com.shahar91.foodwatcher.ui.addFoodItem

import androidx.lifecycle.MutableLiveData
import be.appwise.core.ui.base.BaseViewModel

class AddFoodItemViewModel : BaseViewModel() {
    var name = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var points = MutableLiveData<String>()
}