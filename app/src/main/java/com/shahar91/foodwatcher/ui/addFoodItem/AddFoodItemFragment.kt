package com.shahar91.foodwatcher.ui.addFoodItem

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import be.appwise.core.extensions.fragment.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.databinding.FragmentAddFoodItemBinding
import com.shahar91.foodwatcher.ui.AppBaseBindingVMFragment
import com.shahar91.foodwatcher.utils.CommonUtils
import java.util.*

class AddFoodItemFragment : AppBaseBindingVMFragment<FragmentAddFoodItemBinding>() {
    private val safeArgs: AddFoodItemFragmentArgs by navArgs()

    override val mViewModel: AddFoodItemViewModel by viewModels()
    override fun getLayout() = R.layout.fragment_add_food_item
    override fun getToolbar() = mBinding.mergeToolbar.mtbMain
    override fun getViewModelFactory() = AddFoodItemViewModel.FACTORY(safeArgs.foodItemId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        val toolbarTitle = if (mViewModel.isAddingNew()) {
            "Add item"
        } else {
            "Edit item"
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.title = toolbarTitle

        initViews()
    }

    private fun initViews() {
        mBinding.run {
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

                mViewModel.saveFoodItem(
                    name!!,
                    description ?: "",
                    CommonUtils.getNumberAsFloat(points ?: "0")
                ) {
                    hideKeyboard()
                    findNavController().popBackStack()

                    showFeedbackToUser()
                }
            }
        }
    }

    private fun showFeedbackToUser() {
        val feedback = if (mViewModel.isAddingNew()) {
            "Item was added successfully!"
        } else {
            "Changes have been successfully saved!"
        }

        Snackbar.make(mBinding.btnSaveFoodItem, feedback, Snackbar.LENGTH_SHORT).show()
    }
}