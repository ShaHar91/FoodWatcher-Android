package com.shahar91.foodwatcher.ui.addFoodItem

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import be.appwise.core.extensions.fragment.hideKeyboard
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.databinding.FragmentAddFoodItemBinding
import com.shahar91.foodwatcher.ui.AppBaseBindingVMFragment
import java.math.BigDecimal

class AddFoodItemFragment : AppBaseBindingVMFragment<AddFoodItemViewModel, FragmentAddFoodItemBinding>() {

    override fun getViewModel() = AddFoodItemViewModel::class.java
    override fun getLayout() = R.layout.fragment_add_food_item
    override fun getToolbar() = mBinding.mergeToolbar.mtbMain

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

                mViewModel.saveFoodItem(name!!, description ?: "", points?.toFloat()?.round(1) ?: 0F) {
                    hideKeyboard()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun Float.round(decimals: Int = 2): Float = "%.${decimals}f".format(this).toFloat()
}