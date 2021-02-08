package com.shahar91.foodwatcher.ui.addFoodItem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import be.appwise.core.ui.base.BaseBindingVMFragment
import be.appwise.core.ui.base.BaseVMFragment
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.databinding.AddFoodItemFragmentBinding

class AddFoodItemFragment : BaseBindingVMFragment<AddFoodItemViewModel, AddFoodItemFragmentBinding>() {

    override fun getViewModel() = AddFoodItemViewModel::class.java
    override fun getLayout() = R.layout.add_food_item_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        initViews()
    }

    private fun initViews() {
        mBinding.apply {
            btnSaveFoodItem.setOnClickListener {
                Log.d("AddFoodItem", "name: ${mViewModel.name.value}")
                Log.d("AddFoodItem", "description: ${mViewModel.description.value}")
                Log.d("AddFoodItem", "points: ${mViewModel.points.value}")


            }
        }
    }
}