package com.shahar91.foodwatcher.ui.myDay.calendar

import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.shahar91.foodwatcher.databinding.CvMonthViewBinding
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

class MonthViewHeaderBinder : MonthHeaderFooterBinder<MonthViewHeaderContainer> {
    override fun create(view: View) = MonthViewHeaderContainer(view)
    override fun bind(container: MonthViewHeaderContainer, month: CalendarMonth) {
        // Setup each header day text if we have not done that already.
        if (container.legendLayout.tag == null) {
            container.legendLayout.tag = month.yearMonth
            container.legendLayout.children.map { it as TextView }
                .forEachIndexed { index, tv ->
                    tv.text = daysOfWeekFromLocale()[index].getDisplayName(TextStyle.NARROW_STANDALONE, Locale.getDefault())
                }
        }
    }

    private fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = DayOfWeek.MONDAY
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }
}

class MonthViewHeaderContainer(view: View) : ViewContainer(view) {
    val legendLayout = CvMonthViewBinding.bind(view).legendLayout
}