package com.shahar91.foodwatcher.ui.myDay

import be.appwise.core.ui.base.BaseViewModel
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

class MyDayViewModel : BaseViewModel() {
    fun getCorrectMonthAsString(calendarMonth: CalendarMonth): String {
        val weekDays = calendarMonth.weekDays[0]
        val firstDay = weekDays.first()
        val lastDay = weekDays.last()

        val groupedByMonth: Map<Month, List<CalendarDay>> = calendarMonth.weekDays[0].groupBy { it.date.month }

        return if (groupedByMonth.size == 1 || groupedByMonth[firstDay.date.month]?.size ?: 0 > groupedByMonth[lastDay.date.month]?.size ?: 0) {
            getCalendarMonthHeaderFormatter(firstDay.date)
        } else {
            getCalendarMonthHeaderFormatter(lastDay.date)
        }
    }

    private fun getCalendarMonthHeaderFormatter(date: LocalDate): String {
        return DateTimeFormatter.ofPattern("MMMM yyyy").format(date)
    }
}