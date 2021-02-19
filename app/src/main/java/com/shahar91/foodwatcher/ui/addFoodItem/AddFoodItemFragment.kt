package com.shahar91.foodwatcher.ui.addFoodItem

import android.os.Bundle
import android.view.View
import be.appwise.core.extensions.fragment.hideKeyboard
import be.appwise.core.ui.base.BaseBindingVMFragment
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.databinding.FragmentAddFoodItemBinding

class AddFoodItemFragment : BaseBindingVMFragment<AddFoodItemViewModel, FragmentAddFoodItemBinding>() {

    override fun getViewModel() = AddFoodItemViewModel::class.java
    override fun getLayout() = R.layout.fragment_add_food_item

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

                val name = mViewModel.name.value
                if (name.isNullOrBlank()) {
                    mViewModel.name.value = ""
                    isSomethingEmpty = true
                }

                val description = mViewModel.description.value

                val points = mViewModel.points.value
                if (points.isNullOrBlank()) {
                    mViewModel.points.value = ""
                    isSomethingEmpty = true
                }

                if (isSomethingEmpty) {
                    return@setOnClickListener
                }

                mViewModel.saveFoodItem(name!!, description ?: "", points?.toFloat()!!) {
                    hideKeyboard()
                    requireActivity().onBackPressed()
                }
            }
        }
    }
}