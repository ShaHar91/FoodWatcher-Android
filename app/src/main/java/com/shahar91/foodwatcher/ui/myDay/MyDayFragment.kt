package com.shahar91.foodwatcher.ui.myDay

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import be.appwise.core.extensions.view.setupRecyclerView
import be.appwise.emptyRecyclerView.RecyclerViewState
import com.google.android.material.snackbar.Snackbar
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.data.models.DayDescription
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.databinding.FragmentMyDayBinding
import com.shahar91.foodwatcher.ui.AppBaseBindingVMFragment
import com.shahar91.foodwatcher.ui.myDay.adapter.FoodEntryAdapter
import com.shahar91.foodwatcher.ui.myDay.adapter.ViewTypeItemDecoration
import com.shahar91.foodwatcher.ui.myDay.calendar.binders.DayViewBinder
import com.shahar91.foodwatcher.ui.myDay.calendar.binders.MonthViewHeaderBinder
import com.shahar91.foodwatcher.utils.DialogFactory
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class MyDayFragment : AppBaseBindingVMFragment<FragmentMyDayBinding>() {

    override val mViewModel: MyDayViewModel by viewModels()
    override fun getLayout() = R.layout.fragment_my_day
    override fun getToolbar() = mBinding.mtbMain

    private val foodEntryAdapterListener = object : FoodEntryAdapter.FoodEntryInteractionListener {
        override fun onFoodEntryClicked(foodEntry: FoodEntry) {
            //TODO: -1 foodItemId won't cause any breaking issues, but will not fetch the correct foodItem values...
            // show a dialog to let the user know it's at their own risk...
            MyDayFragmentDirections.actionMyDayFragmentToAddMealEntryFragment(foodEntry.foodItemId, foodEntry.id)
                .let(findNavController()::navigate)
        }

        override fun onFoodEntryLongClicked(foodEntry: FoodEntry) {
            deleteFoodEntry(foodEntry)
        }
    }
    private val foodEntryAdapter: FoodEntryAdapter = FoodEntryAdapter(foodEntryAdapterListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        initCalendar()
        initListeners()
        initViews()

        mViewModel.items.observe(viewLifecycleOwner, {
            foodEntryAdapter.addHeaderAndSubmitList(it)
            mViewModel.updateTotalPoints(it)
            mViewModel.updateWeekTotalPoints()
        })

        mViewModel.myDayDescription.observe(viewLifecycleOwner, {
            requireActivity().invalidateOptionsMenu()
        })
    }

    private fun initViews() {
        mBinding.run {
            rvFoodEntries.run {
                setupRecyclerView(decoration = ViewTypeItemDecoration(requireContext(), ViewTypeItemDecoration.VERTICAL).apply {
                    dividerForItemTypes = listOf(FoodEntryAdapter.ITEM_VIEW_TYPE_ITEM)
                    showDividerLastItem = false
                })
                adapter = foodEntryAdapter
                state = RecyclerViewState.NORMAL
            }
        }
    }

    private fun initListeners() {
        mBinding.fabAddFoodItemToDay.setOnClickListener {
            MyDayFragmentDirections.actionMyDayFragmentToFoodItemListFragment().run(findNavController()::navigate)
        }
    }

    private fun initCalendar() {
        mBinding.cvCalendar.run {
            val currentMonth = YearMonth.now()
            setup(currentMonth.minusMonths(120), currentMonth.plusMonths(120), DayOfWeek.MONDAY)
            scrollToDate(LocalDate.now())
            dayBinder = this@MyDayFragment.dayBinder
            monthHeaderBinder = this@MyDayFragment.monthHeaderBinder
            selectDate(LocalDate.now())
            monthScrollListener = {
                mBinding.tvMonth.text = mViewModel.getCorrectMonthAsString(it)
            }
        }
    }

    private val dayBinder: DayViewBinder by lazy {
        DayViewBinder(::selectDate)
    }

    private val monthHeaderBinder: MonthViewHeaderBinder by lazy {
        MonthViewHeaderBinder()
    }

    private fun selectDate(localDate: LocalDate) {
        if (dayBinder.selectedDate != localDate) {
            val oldDate = dayBinder.selectedDate
            dayBinder.selectedDate = localDate
            oldDate?.let { mBinding.cvCalendar.notifyDateChanged(it) }
            mBinding.cvCalendar.notifyDateChanged(localDate)
            mBinding.viewModel?.setSelectedDate(localDate)
        }
    }

    private fun deleteFoodEntry(foodEntry: FoodEntry) {
        DialogFactory.showConfirmationDialog(
            requireContext(),
            "Delete ${foodEntry.foodItemName}",
            "This will delete the entry for '${foodEntry.foodItemName}' for eternity, are you sure you want to delete it?"
        ) {
            mViewModel.deleteFoodEntry(foodEntry) {
                Snackbar.make(mBinding.root, "Item was removed successfully!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showInformationDialog(dayDescription: DayDescription?) {
        DialogFactory.showInputInformationDialog(requireContext(), dayDescription?.description ?: "") { updatedDescription ->
            mViewModel.updateDayDescription(updatedDescription)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.my_day_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_information).setIcon(mViewModel.getInformationIcon())

        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_information) {
            showInformationDialog(mViewModel.myDayDescription.value)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}