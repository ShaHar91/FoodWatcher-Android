package com.shahar91.foodwatcher.ui.myDay.calendar.containers

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import com.shahar91.foodwatcher.R
import java.time.LocalDate

class DayViewContainer(view: View, selectDate: (date: LocalDate) -> Unit) : ViewContainer(view) {
    lateinit var day: CalendarDay // Will be set when this container is bound.
    val textView: TextView = view.findViewById(R.id.tvDayNumber)
    val dotView: View = view.findViewById(R.id.vDot)

    init {
        view.setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH) {
                selectDate(day.date)
            }
        }
    }
}