package com.shahar91.foodwatcher.utils

import be.appwise.core.util.HawkValueDelegate

object HawkManager {
    private const val HAWK_MAX_DAY_TOTAL = "maxDayTotal"
    var hawkMaxDayTotal : Int by HawkValueDelegate(HAWK_MAX_DAY_TOTAL, 29)
    private const val HAWK_MAX_WEEK_TOTAL = "maxWeekTotal"
    var hawkMaxWeekTotal : Int by HawkValueDelegate(HAWK_MAX_WEEK_TOTAL, 49)
}