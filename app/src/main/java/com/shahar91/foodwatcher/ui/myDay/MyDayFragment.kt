package com.shahar91.foodwatcher.ui.myDay

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import be.appwise.core.extensions.view.setupRecyclerView
import be.appwise.emptyRecyclerView.RecyclerViewState
import com.google.android.material.snackbar.Snackbar
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.data.models.DayDescription
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.databinding.FragmentMyDayBinding
import com.shahar91.foodwatcher.ui.base.AppBaseBindingVMFragment
import com.shahar91.foodwatcher.ui.myDay.adapter.FoodEntryAdapter
import com.shahar91.foodwatcher.ui.myDay.adapter.ViewTypeItemDecoration
import com.shahar91.foodwatcher.ui.myDay.calendar.DayViewBinder
import com.shahar91.foodwatcher.ui.myDay.calendar.MonthViewHeaderBinder
import com.shahar91.foodwatcher.utils.DialogFactory
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class MyDayFragment : AppBaseBindingVMFragment<FragmentMyDayBinding>() {

    override val mViewModel: MyDayViewModel by viewModel()
    override fun getLayout() = R.layout.fragment_my_day
    override fun getToolbar() = mBinding.mtbMain

    private val dayBinder: DayViewBinder by lazy { DayViewBinder(::selectDate) }
    private val monthHeaderBinder: MonthViewHeaderBinder by lazy { MonthViewHeaderBinder() }

    private val foodEntryAdapter: FoodEntryAdapter = FoodEntryAdapter(object : FoodEntryAdapter.FoodEntryInteractionListener {
        override fun onFoodEntryClicked(foodEntry: FoodEntry) {
            //TODO: -1 foodItemId won't cause any breaking issues, but will not fetch the correct foodItem values...
            // show a dialog to let the user know it's at their own risk...
            MyDayFragmentDirections.actionMyDayFragmentToAddMealEntryFragment(foodEntry.foodItemId, foodEntry.id)
                .let(findNavController()::navigate)
        }

        override fun onFoodEntryLongClicked(foodEntry: FoodEntry) {
            deleteFoodEntry(foodEntry)
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.run {
            viewModel = mViewModel
        }

        initListeners()
        initViews()
        initObservers()
    }

    private fun initObservers() {
        mViewModel.let {

            it.items.observe(viewLifecycleOwner) { list ->
                foodEntryAdapter.addHeaderAndSubmitList(requireContext(), list)
            }

            it.myDayDescription.observe(viewLifecycleOwner) {
                requireActivity().invalidateOptionsMenu()
            }

            it.monthEntries.observe(viewLifecycleOwner) { list ->
                dayBinder.events = list
                list.forEach { date ->
                    mBinding.cvCalendar.notifyDateChanged(date)
                }
            }
        }
    }

    private fun initViews() {
        initCalendar()

        mBinding.run {
            rvFoodEntries.let {
                it.setupRecyclerView(decoration = ViewTypeItemDecoration(requireContext(), ViewTypeItemDecoration.VERTICAL).apply {
                    dividerForItemTypes = listOf(FoodEntryAdapter.FoodEntryTypes.FOOD_ENTRY.id)
                    showDividerLastItem = false
                })
                it.adapter = foodEntryAdapter
                it.state = RecyclerViewState.NORMAL
                it.emptyStateView = llEmptyView
            }
        }
    }

    private fun initListeners() {
        mBinding.fabAddFoodItemToDay.setOnClickListener {
            MyDayFragmentDirections.actionMyDayFragmentToFoodItemListFragment().run(findNavController()::navigate)
        }
    }

    private fun initCalendar() {
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(120)
        val lastMonth = currentMonth.plusMonths(120)
        val firstDayOfWeek = DayOfWeek.MONDAY //TODO: make this a setting??
        val selectedDate = mViewModel.calendarDay.value ?: LocalDate.now()

        mBinding.cvCalendar.let {
            it.dayBinder = dayBinder
            it.monthHeaderBinder = monthHeaderBinder

            it.setup(firstMonth, lastMonth, firstDayOfWeek)
            it.scrollToDate(selectedDate)
            it.monthScrollListener = { calendarMonth ->
                mBinding.tvMonth.text = mViewModel.getCorrectMonthAsString(calendarMonth)
                mViewModel.setMonthEntries(calendarMonth)
            }
            selectDate(selectedDate)
        }
    }

    private fun selectDate(localDate: LocalDate) {
        // only change things when another date has been selected
        if (dayBinder.selectedDate != localDate) {
            // Save the previous date temporarily
            val oldDate = dayBinder.selectedDate

            dayBinder.selectedDate = localDate

            // Notify the Calendar about both Date Changes
            oldDate?.let { mBinding.cvCalendar.notifyDateChanged(it) }
            mBinding.cvCalendar.notifyDateChanged(localDate)

            // Save the Selected Date in the ViewModel
            mBinding.viewModel?.setSelectedDate(localDate)
        }
    }

    private fun deleteFoodEntry(foodEntry: FoodEntry) {
        DialogFactory.showConfirmationDialog(
            requireContext(),
            getString(R.string.dialog_common_delete_item_title, foodEntry.foodItemName),
            getString(R.string.my_day_delete_entry_content, foodEntry.foodItemName)
        ) {
            mViewModel.deleteFoodEntry(foodEntry) {
                Snackbar.make(mBinding.root, getString(R.string.my_day_delete_entry_successful), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showInformationDialog(dayDescription: DayDescription?) {
        DialogFactory.showInputInformationDialog(requireContext(), dayDescription?.description ?: "") { updatedDescription ->
            mViewModel.updateDayDescription(updatedDescription)
        }
    }

    // <editor-fold desc="menu">
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.my_day_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_information) {
            showInformationDialog(mViewModel.myDayDescription.value)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_information).setIcon(mViewModel.getInformationIcon())

        super.onPrepareOptionsMenu(menu)
    }
    // </editor-fold>
}