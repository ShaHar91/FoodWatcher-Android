package com.shahar91.foodwatcher.ui.myDay.calendar

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import com.google.android.material.color.MaterialColors
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.databinding.CvDayViewBinding
import java.time.LocalDate

class DayViewBinder(private val selectDate: (data: LocalDate) -> Unit) : DayBinder<DayViewContainer> {
    var selectedDate: LocalDate? = null

    override fun create(view: View) = DayViewContainer(view) {
        selectDate(it)
    }

    override fun bind(container: DayViewContainer, day: CalendarDay) {
        container.day = day

        container.textView.text = day.date.dayOfMonth.toString()

        when (day.date) {
            selectedDate ->
                styleDateLayout(container, R.attr.colorOnPrimarySurface, R.drawable.bg_calendar_day_filled)
            LocalDate.now() ->
                styleDateLayout(container, R.attr.colorOnSurface, R.drawable.bg_calendar_day_today)
            else ->
                styleDateLayout(container, R.attr.colorOnSurface, R.drawable.bg_calendar_day_filled, R.attr.colorBackground)
        }
    }

    private fun styleDateLayout(
        container: DayViewContainer,
        @AttrRes textColor: Int,
        @DrawableRes backGroundRes: Int,
        @AttrRes backgroundColorRes: Int? = null
    ) {
        container.textView.setTextColor(MaterialColors.getColor(container.textView, textColor))

        container.llDayView.setBackgroundResource(backGroundRes)
        container.llDayView.backgroundTintList = backgroundColorRes?.let { ColorStateList.valueOf(MaterialColors.getColor(container.llDayView, it)) }
    }
}

class DayViewContainer(view: View, selectDate: (date: LocalDate) -> Unit) : ViewContainer(view) {
    lateinit var day: CalendarDay // Will be set when this container is bound.
    private val binding = CvDayViewBinding.bind(view)
    val textView: TextView = binding.tvDayNumber
    val dotView: View = binding.vDot
    val llDayView = binding.llDayView

    init {
        view.setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH) {
                selectDate(day.date)
            } else {
                Log.d("DayViewContainer", "It's not possible to select outDates (dates that do not belong to this month). Not a problem for our use-case for know.")
            }
        }
    }
}