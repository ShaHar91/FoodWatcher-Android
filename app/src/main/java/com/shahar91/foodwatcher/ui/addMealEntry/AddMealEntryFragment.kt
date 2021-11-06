package com.shahar91.foodwatcher.ui.addMealEntry

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import be.appwise.core.extensions.fragment.hideKeyboard
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.databinding.FragmentAddMealEntryBinding
import com.shahar91.foodwatcher.ui.AppBaseBindingVMFragment
import com.shahar91.foodwatcher.utils.CommonUtils
import com.shahar91.foodwatcher.utils.DialogFactory

class AddMealEntryFragment : AppBaseBindingVMFragment<FragmentAddMealEntryBinding>() {
    private val safeArgs: AddMealEntryFragmentArgs by navArgs()

    override fun getToolbar() = mBinding.mergeToolbar.mtbMain
    override fun getLayout() = R.layout.fragment_add_meal_entry
    override val mViewModel: AddMealEntryViewModel by viewModels()
    override fun getViewModelFactory() = AddMealEntryViewModel.FACTORY(safeArgs.foodItemId, safeArgs.foodEntryId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        initObservers()
        initViews()
    }

    private fun initObservers() {
        mViewModel.foodItem.observe(viewLifecycleOwner, {
            requireActivity().invalidateOptionsMenu()
        })
    }

    private fun initViews() {
        mBinding.run {
            tilDate.editText?.setOnClickListener {
                // For more information about the MaterialDatePicker
                //      https://material.io/components/date-pickers
                //      https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/datepicker/DatePickerMainDemoFragment.java
                val picker = MaterialDatePicker.Builder.datePicker().setSelection(mViewModel.selectedDateMillis.value).build()
                picker.addOnPositiveButtonClickListener {
                    mViewModel.setSelectedDateMillis(it)
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

                if (isSomethingEmpty) {
                    return@setOnClickListener
                }

                mViewModel.saveMealEntry(CommonUtils.getNumberAsFloat(servingSize ?: "0")) {
                    val message = if (mViewModel.isAddingNew()) "Item was added successfully!" else "Item was changed successfully!"

                    exitFragmentAndShowMessage(message)
                }
            }
        }
    }

    private fun deleteFoodItem() {
        val itemName = mViewModel.foodItem.value?.name ?: ""

        DialogFactory.showConfirmationDialog(
            requireContext(),
            "Delete $itemName",
            "This will delete '$itemName' for eternity, are you sure you want to delete it?"
        ) {
            mViewModel.deleteFoodItem {
                exitFragmentAndShowMessage("Item was removed successfully!")
            }
        }
    }

    private fun deleteFoodEntry() {
        val foodEntry = mViewModel.foodEntry

        DialogFactory.showConfirmationDialog(
            requireContext(),
            "Delete ${foodEntry.foodItemName}",
            "This will delete the entry for '${foodEntry.foodItemName}' for eternity, are you sure you want to delete it?"
        ) {
            mViewModel.deleteFoodEntry(foodEntry) {
                exitFragmentAndShowMessage("Item was removed successfully!")
            }
        }
    }

    private fun exitFragmentAndShowMessage(message: String) {
        hideKeyboard()
        findNavController().popBackStack()

        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_SHORT).show()
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

        menu.findItem(R.id.action_editFoodItem).let {
            if (!mViewModel.isAddingNew()) {
                it.isVisible = false
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
            R.id.action_deleteFoodItem -> {
                if (mViewModel.isAddingNew()) {
                    deleteFoodItem()
                } else {
                    deleteFoodEntry()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}