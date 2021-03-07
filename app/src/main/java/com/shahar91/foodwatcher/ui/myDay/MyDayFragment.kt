package com.shahar91.foodwatcher.ui.myDay

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import be.appwise.core.extensions.view.setupRecyclerView
import be.appwise.core.ui.custom.RecyclerViewEnum
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.databinding.FragmentMyDayBinding
import com.shahar91.foodwatcher.ui.AppBaseBindingVMFragment
import com.shahar91.foodwatcher.ui.myDay.adapter.FoodEntryAdapter
import com.shahar91.foodwatcher.ui.myDay.calendar.binders.DayViewBinder
import com.shahar91.foodwatcher.ui.myDay.calendar.binders.MonthViewHeaderBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class MyDayFragment : AppBaseBindingVMFragment<MyDayViewModel, FragmentMyDayBinding>() {

    override fun getViewModel() = MyDayViewModel::class.java
    override fun getLayout() = R.layout.fragment_my_day
    override fun getToolbar() = mBinding.mtbMain

    private val foodEntryAdapterListener = object : FoodEntryAdapter.FoodEntryInteractionListener {
        override fun onFoodEntryClicked(foodEntry: FoodEntry) {

        }
    }
    private val foodEntryAdapter: FoodEntryAdapter = FoodEntryAdapter(foodEntryAdapterListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        initCalendar()
        initListeners()
        initViews()

        mBinding.viewModel?.items?.observe(viewLifecycleOwner, {
            Log.d("MyDayFragment", "onViewCreated: $it")
            foodEntryAdapter.addHeaderAndSubmitList(it)
        })
    }

    private fun initViews() {
        mBinding.apply {
            rvFoodEntries.apply {
                //TODO: don't show the divider for a certain viewType: https://stackoverflow.com/a/46216274/2263408
                setupRecyclerView()
                adapter = foodEntryAdapter
                stateView = RecyclerViewEnum.NORMAL
            }
        }
    }

    private fun initListeners() {
        mBinding.fabAddFoodItemToDay.setOnClickListener {
            MyDayFragmentDirections.actionMyDayFragmentToFoodItemListFragment().run(findNavController()::navigate)
        }
    }

    private fun initCalendar() {
        mBinding.cvCalendar.apply {
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
}