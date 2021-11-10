package com.shahar91.foodwatcher.ui.myDay.calendar.calendar

import android.content.res.ColorStateList
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
import java.time.LocalDate

class DayViewBinder(private val selectDate: (data: LocalDate) -> Unit) : DayBinder<DayViewContainer> {
    var selectedDate: LocalDate? = null

    override fun create(view: View) = DayViewContainer(view) {
        selectDate(it)
    }

    override fun bind(container: DayViewContainer, day: CalendarDay) {
        container.day = day
        val textView = container.textView
        val dotView = container.dotView

        textView.text = day.date.dayOfMonth.toString()

        when (day.date) {
            selectedDate -> {
                styleDateTextView(textView, R.attr.colorOnPrimarySurface, R.drawable.ic_circle, R.attr.colorPrimary)
            }
            LocalDate.now() -> {
                styleDateTextView(textView, R.attr.colorOnSurface, R.drawable.ic_circle_bordered, R.attr.colorPrimary)
            }
            else -> {
                styleDateTextView(textView, R.attr.colorOnSurface)
            }
        }
    }

    private fun styleDateTextView(
        textView: TextView,
        @AttrRes textColor: Int,
        @DrawableRes backGroundRes: Int? = null,
        @AttrRes backgroundColorRes: Int = -1
    ) {
        textView.setTextColor(MaterialColors.getColor(textView, textColor))
        if (backGroundRes != null) {
            textView.setBackgroundResource(backGroundRes)
            textView.backgroundTintList = ColorStateList.valueOf(MaterialColors.getColor(textView, backgroundColorRes))
        } else {
            textView.background = null
        }
    }
}

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