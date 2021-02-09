package com.shahar91.foodwatcher.ui.addFoodItem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.NavigationUI
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
                var isSomethingEmpty = false

                val name= mViewModel.name.value
                if (name.isNullOrBlank()) {
                    mViewModel.name.value = ""
                    isSomethingEmpty = true
                }

                val description = mViewModel.description.value
                if (description.isNullOrBlank()) {
                    mViewModel.description.value = ""
                    isSomethingEmpty = true
                }

                val points = mViewModel.points.value
                if (points.isNullOrBlank()) {
                    mViewModel.points.value = ""
                    isSomethingEmpty = true
                }

                if (isSomethingEmpty) {
                    return@setOnClickListener
                }

                mViewModel.saveFoodItem(name!!, description!!, points?.toFloat()!!) {
                    requireActivity().onBackPressed()
                }
            }
        }
    }
}