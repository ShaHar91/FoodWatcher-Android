package com.shahar91.foodwatcher.ui.addMealEntry

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import be.appwise.core.extensions.fragment.hideKeyboard
import com.google.android.material.datepicker.MaterialDatePicker
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.data.models.Meal
import com.shahar91.foodwatcher.databinding.FragmentAddMealEntryBinding
import com.shahar91.foodwatcher.ui.AppBaseBindingVMFragment

class AddMealEntryFragment : AppBaseBindingVMFragment<AddMealEntryViewModel, FragmentAddMealEntryBinding>() {
    private val safeArgs: AddMealEntryFragmentArgs by navArgs()

    override fun getToolbar() = mBinding.mergeToolbar.mtbMain
    override fun getLayout() = R.layout.fragment_add_meal_entry
    override fun getViewModel() = AddMealEntryViewModel::class.java
    override fun getViewModelFactory() = AddMealEntryViewModel.FACTORY(safeArgs.foodItemId)

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
            tilDate.editText?.setOnClickListener {
                // For more information about the MaterialDatePicker
                //      https://material.io/components/date-pickers
                //      https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/datepicker/DatePickerMainDemoFragment.java
                val picker = MaterialDatePicker.Builder.datePicker().setSelection(viewModel?.selectedDateMillis).build()
                picker.addOnPositiveButtonClickListener {
                    viewModel?.dateFormat(it)
                }
                picker.show(childFragmentManager, picker.toString())
            }

            btnSaveMealEntry.setOnClickListener {
                var isSomethingEmpty = false

                val servingSize = mViewModel.servingSize.value
                if (servingSize.isNullOrBlank()) {
                    mViewModel.servingSize.value = ""
                    isSomethingEmpty = true
                }

                val selectedDateMillis = mViewModel.selectedDateMillis

                val meal = when (btnGrMeal.checkedButtonId) {
                    R.id.btnBreakfast -> Meal.BREAKFAST
                    R.id.btnDinner -> Meal.DINNER
                    R.id.btnLunch -> Meal.LUNCH
                    else -> Meal.SNACK
                }


                if (isSomethingEmpty) {
                    return@setOnClickListener
                }

                mViewModel.saveMealEntry(servingSize?.toInt() ?: 0, selectedDateMillis, meal) {
                    hideKeyboard()
                    findNavController().popBackStack()
                }
            }
        }
    }
}