package com.shahar91.foodwatcher.ui.myDay

import android.os.Bundle
import android.view.View
import be.appwise.core.ui.base.BaseBindingVMFragment
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.databinding.FragmentMyDayBinding
import com.shahar91.foodwatcher.ui.myDay.calendar.binders.DayViewBinder
import com.shahar91.foodwatcher.ui.myDay.calendar.binders.MonthViewHeaderBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class MyDayFragment : BaseBindingVMFragment<MyDayViewModel, FragmentMyDayBinding>() {

    override fun getViewModel() = MyDayViewModel::class.java
    override fun getLayout() = R.layout.fragment_my_day

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        initCalendar()
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
        }
    }
}