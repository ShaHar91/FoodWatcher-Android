package com.shahar91.foodwatcher.ui.addMealEntry

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import be.appwise.core.extensions.fragment.hideKeyboard
import be.appwise.core.extensions.fragment.snackBar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.data.models.Meal
import com.shahar91.foodwatcher.databinding.FragmentAddMealEntryBinding
import com.shahar91.foodwatcher.ui.AppBaseBindingVMFragment
import com.shahar91.foodwatcher.utils.CommonUtils

class AddMealEntryFragment : AppBaseBindingVMFragment<AddMealEntryViewModel, FragmentAddMealEntryBinding>() {
    private val safeArgs: AddMealEntryFragmentArgs by navArgs()

    override fun getToolbar() = mBinding.mergeToolbar.mtbMain
    override fun getLayout() = R.layout.fragment_add_meal_entry
    override fun getViewModel() = AddMealEntryViewModel::class.java
    override fun getViewModelFactory() = AddMealEntryViewModel.FACTORY(safeArgs.foodItemId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        mViewModel.foodItem.observe(viewLifecycleOwner, Observer {
            requireActivity().invalidateOptionsMenu()
        })

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

                mViewModel.saveMealEntry(CommonUtils.getNumberAsFloat(servingSize ?: "0"), selectedDateMillis, meal) {
                    hideKeyboard()
                    findNavController().popBackStack()

                    Snackbar.make(btnSaveMealEntry, "Item was added successfully!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_food_entry_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // For more info about changing menu items at runtime see
        // https://developer.android.com/guide/topics/ui/menus#ChangingTheMenu
        menu.findItem(R.id.action_favoriteFoodItem).let {
            if (mViewModel.foodItem.value?.isFavorite == true) {
                it.setIcon(R.drawable.ic_favorite_filled)
            } else {
                it.setIcon(R.drawable.ic_favorite_outline)
            }
        }

        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favoriteFoodItem -> {
                mViewModel.toggleFavoriteFoodItem()
                true
            }
            R.id.action_editFoodItem -> {
                AddMealEntryFragmentDirections.actionAddMealEntryFragmentToAddFoodItemFragment(safeArgs.foodItemId).run(findNavController()::navigate)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}