package com.shahar91.foodwatcher.ui.myDay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import be.appwise.core.ui.base.BaseViewModel
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.utils.yearMonth
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.data.models.DayDescription
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.repository.DayDescriptionRepository
import com.shahar91.foodwatcher.data.repository.FoodEntryRepository
import com.shahar91.foodwatcher.utils.CommonUtils
import com.shahar91.foodwatcher.utils.HawkManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.*
import java.time.format.DateTimeFormatter

class MyDayViewModel(
    private val dayDescriptionRepository: DayDescriptionRepository,
    private val foodEntryRepository: FoodEntryRepository
) : BaseViewModel() {

    private val _calendarDay = MutableLiveData<LocalDate>().apply { value = LocalDate.now() }
    val calendarDay: LiveData<LocalDate> get() = _calendarDay
    fun setSelectedDate(date: LocalDate) = _calendarDay.postValue(date)

    // Gets updated whenever the 'selectedDay' changes
    val items = Transformations.switchMap(_calendarDay) { foodEntryRepository.getFoodEntries(it.atStartOfDayMillis(), it.atEndOfDayMillis()) }

    // Gets updated with a new 'dayDescription' when the 'selectedDay' changes
    val myDayDescription = Transformations.switchMap(_calendarDay) {
        dayDescriptionRepository.getDescriptionForDay(it.atStartOfDayMillis(), it.atEndOfDayMillis())
    }

    // When the list of items gets updated the 'weekTotal' gets updated as well
    val weekTotal = Transformations.map(items) { updateWeekTotalPoints() }

    // When the list of items gets updated the 'dayTotal' gets updated as well
    val dayTotal = Transformations.map(items) { updateTotalPoints(it) }

    private var _fetchForMonth = MutableLiveData<CalendarMonth>()
    private var fetchForMonthJob: Job? = null
    fun setMonthEntries(calendarMonth: CalendarMonth) {
        fetchForMonthJob?.cancel()
        fetchForMonthJob = vmScope.launch {
            delay(750)
            _fetchForMonth.postValue(calendarMonth)
        }
    }

    val monthEntries = Transformations.switchMap(_fetchForMonth) { month ->
        val startMonth = month.weekDays.first().first().date.yearMonth.atDay(1).minusMonths(1).atStartOfDayMillis()
        val endMonth = month.weekDays.last().last().date.yearMonth.atEndOfMonth().plusMonths(1).atEndOfDayMillis()

        foodEntryRepository.getFoodEntriesFlow(startMonth, endMonth)
            .map {
                it.map { item ->
                    Instant.ofEpochMilli(item.date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }.distinct()
            }
            .asLiveData()
    }

    /**
     * The Calendar can show multiple months in 1 line (in week mode).
     * Because of that, we will check if the current [calendarMonth] has more days in the first week.
     * In case it has the currentMonth will be returned as a String, otherwise the other month will be shown.
     *
     * @param calendarMonth Current visible month in the calendar (Week mode)
     * @return Month formatted as a String
     */
    fun getCorrectMonthAsString(calendarMonth: CalendarMonth): String {
        val weekDays = calendarMonth.weekDays[0]
        val firstDay = weekDays.first().date
        val lastDay = weekDays.last().date

        val groupedByMonth: Map<Month, List<CalendarDay>> = calendarMonth.weekDays[0].groupBy { it.date.month }

        return if (groupedByMonth.size == 1 || groupedByMonth[firstDay.month]?.size ?: 0 > groupedByMonth[lastDay.month]?.size ?: 0) {
            getCalendarMonthHeaderFormatter(firstDay)
        } else {
            getCalendarMonthHeaderFormatter(lastDay)
        }
    }

    fun getInformationIcon() = if (myDayDescription.value != null) {
        R.drawable.ic_info_available
    } else {
        R.drawable.ic_info_unavailable
    }

    private fun getCalendarMonthHeaderFormatter(date: LocalDate) =
        DateTimeFormatter.ofPattern("MMMM yyyy").format(date)

    fun deleteFoodEntry(foodEntry: FoodEntry, onSuccess: () -> Unit) = vmScope.launch {
        foodEntryRepository.deleteFoodEntry(foodEntry)
        onSuccess()
    }

    private fun updateTotalPoints(foodEntries: List<FoodEntry>): String {
        val totalPointsAsDouble = foodEntries.sumOf { it.amount.toDouble() * it.foodItemPoints.toDouble() }
        return CommonUtils.showValueWithoutTrailingZero(HawkManager.hawkMaxDayTotal - totalPointsAsDouble.toFloat())
    }

    private fun updateWeekTotalPoints(): String {
        val weekTotal: Float = runBlocking { foodEntryRepository.getWeekTotal(_calendarDay.value?.atStartOfWeekMillis() ?: 0L) }
        return CommonUtils.showValueWithoutTrailingZero(weekTotal)
    }

    fun updateDayDescription(updatedDescription: String) = vmScope.launch {
        val dayDescription = myDayDescription.value

        if (updatedDescription == dayDescription?.description ?: "") {
            // Description hasn't been changed so nothing should happen with it.
            return@launch
        }

        if (updatedDescription.isNotBlank()) {
            // If some text was added, persist the data in Room
            val updateDayDescription = dayDescription?.apply { description = updatedDescription }
            // In case a day didn't have a description text, this will be executed
                ?: DayDescription(description = updatedDescription, date = _calendarDay.value?.atMiddleOfDayMillis() ?: 0L)

            dayDescriptionRepository.createDayDescription(updateDayDescription)
        } else if (dayDescription != null) {
            // If the text was emptied, remove the data in Room
            dayDescriptionRepository.deleteDayDescription(dayDescription)
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