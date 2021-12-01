package com.shahar91.foodwatcher.ui.addFoodItem

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import be.appwise.core.extensions.fragment.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.databinding.FragmentAddFoodItemBinding
import com.shahar91.foodwatcher.ui.base.AppBaseBindingVMFragment
import com.shahar91.foodwatcher.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class AddFoodItemFragment : AppBaseBindingVMFragment<FragmentAddFoodItemBinding>() {
    private val safeArgs: AddFoodItemFragmentArgs by navArgs()

    override fun getLayout() = R.layout.fragment_add_food_item
    override fun getToolbar() = mBinding.mtbMain
    override val mViewModel: AddFoodItemViewModel by viewModel {
        parametersOf(safeArgs.foodItemId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        val toolbarTitle = if (mViewModel.isAddingNew()) {
            getString(R.string.add_food_item_toolbar_title)
        } else {
            getString(R.string.add_food_item_edit_toolbar_title)
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
            getString(R.string.add_food_item_saved)
        } else {
            getString(R.string.add_food_item_updated)
        }

        Snackbar.make(mBinding.btnSaveFoodItem, feedback, Snackbar.LENGTH_SHORT).show()
    }
}