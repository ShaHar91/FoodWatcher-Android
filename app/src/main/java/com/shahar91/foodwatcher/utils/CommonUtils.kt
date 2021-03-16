package com.shahar91.foodwatcher.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object CommonUtils {
    /**
     * To format a String to a Float I have to use the DecimalFormat class.
     * Especially if I want to target multiple Locale's.
     *
     * The DecimalSymbol in English is a '.' and for other countries in Europe it is a ','.
     * A ',' does not work well with Room it seems...
     *
     * Also, a bug still persists for Android where only a '.' is possible as a Decimal delimiter
     * For more information see https://stackoverflow.com/q/3821539/2263408
     */
    fun getNumberAsFloat(numberAsString: String): Float {
        return DecimalFormat("####.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH)).parse(numberAsString)?.toFloat() ?: 0F
    }

    /**
     * In order to show the "points" in different Locale's without trailing 0's the DecimalFormat class is used.
     * This will enforce the correct pattern for that Locale
     */
    fun showValueWithoutTrailingZero(value: Float, locale: Locale = Locale.getDefault()): String {
        return DecimalFormat("####.#", DecimalFormatSymbols.getInstance(locale)).format(value)
    }
}