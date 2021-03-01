package com.shahar91.foodwatcher.ui.myDay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.appwise.core.ui.base.BaseViewModel
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.shahar91.foodwatcher.data.repository.FoodEntryRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyDayViewModel : BaseViewModel() {
    private val _calendarDay = MutableLiveData<LocalDate>().apply { value = LocalDate.now() }
    fun setSelectedDate(date: LocalDate) = _calendarDay.postValue(date)
    val items = Transformations.switchMap(_calendarDay) { FoodEntryRepository.getFoodEntries(it.atStartOfDayMillis(), it.atEndOfDayMillis()) }

    private fun LocalDate.atStartOfDayMillis(): Long {
        return this.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun LocalDate.atEndOfDayMillis(): Long {
        return this.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

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