package com.shahar91.foodwatcher.ui.addFoodItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import be.appwise.core.ui.base.BaseVMFragment
import com.shahar91.foodwatcher.R

class AddFoodItemFragment : BaseVMFragment<AddFoodItemViewModel>() {

    override fun getViewModel() = AddFoodItemViewModel::class.java

    private lateinit var viewModel: AddFoodItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.add_food_item_fragment, container, false)
    }
}