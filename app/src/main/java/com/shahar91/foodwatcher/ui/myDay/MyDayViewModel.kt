package com.shahar91.foodwatcher.ui.myDay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.appwise.core.ui.base.BaseViewModel
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.shahar91.foodwatcher.data.models.DayDescription
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.repository.DayDescriptionRepository
import com.shahar91.foodwatcher.data.repository.FoodEntryRepository
import com.shahar91.foodwatcher.utils.CommonUtils
import com.shahar91.foodwatcher.utils.HawkManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyDayViewModel : BaseViewModel() {
    private val _calendarDay = MutableLiveData<LocalDate>().apply { value = LocalDate.now() }
    fun setSelectedDate(date: LocalDate) = _calendarDay.postValue(date)
    val items = Transformations.switchMap(_calendarDay) { FoodEntryRepository.getFoodEntries(it.atStartOfDayMillis(), it.atEndOfDayMillis()) }

    private val _weekTotal = MutableLiveData(CommonUtils.showValueWithoutTrailingZero(HawkManager.hawkMaxWeekTotal.toFloat()))
    val weekTotal: LiveData<String> get() = _weekTotal

    private val _totalPoints = MutableLiveData(CommonUtils.showValueWithoutTrailingZero(HawkManager.hawkMaxDayTotal.toFloat()))
    val totalPoints: LiveData<String> get() = _totalPoints

    val myDayDescription =
        Transformations.switchMap(_calendarDay) { DayDescriptionRepository.getDescriptionForDay(it.atStartOfDayMillis(), it.atEndOfDayMillis()) }

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

    fun deleteFoodEntry(foodEntry: FoodEntry, onSuccess: () -> Unit) = vmScope.launch {
        FoodEntryRepository.deleteFoodEntry(foodEntry)
        onSuccess()
    }

    fun updateTotalPoints(foodEntries: List<FoodEntry>) = vmScope.launch {
        val totalPointsAsDouble = foodEntries.sumOf { it.amount.toDouble() * it.foodItemPoints.toDouble() }
        _totalPoints.postValue(CommonUtils.showValueWithoutTrailingZero(HawkManager.hawkMaxDayTotal - totalPointsAsDouble.toFloat()))
    }

    fun updateWeekTotalPoints() = vmScope.launch {
        val weekTotal = FoodEntryRepository.getWeekTotal(_calendarDay.value?.atStartOfWeekMillis() ?: 0L)
        _weekTotal.postValue(CommonUtils.showValueWithoutTrailingZero(weekTotal))
    }

    fun updateDayDescription(updatedDescription: String) = vmScope.launch {
        val dayDescription = myDayDescription.value

        if (updatedDescription == dayDescription?.description ?: "") {
            return@launch
        }

        if (updatedDescription.isNotBlank()) {
            val updateDayDescription = dayDescription?.apply { description = updatedDescription }
                ?: DayDescription(description = updatedDescription, date = _calendarDay.value?.atMiddleOfDayMillis() ?: 0L)

            DayDescriptionRepository.createDayDescription(updateDayDescription)
        } else if (dayDescription != null) {
            DayDescriptionRepository.deleteDayDescription(dayDescription)
        }
    }

    //<editor-fold desc="LocalDate extensions">
    private fun LocalDate.atStartOfWeekMillis(): Long {
        val dayOfWeek = this.dayOfWeek
        return this.minusDays(dayOfWeek.value.toLong() - 1).atStartOfDayMillis()
    }

    private fun LocalDate.atStartOfDayMillis(): Long {
        return this.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun LocalDate.atMiddleOfDayMillis(): Long {
        return this.atTime(LocalTime.NOON).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun LocalDate.atEndOfDayMillis(): Long {
        return this.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    //</editor-fold>
}