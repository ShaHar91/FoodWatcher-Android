package be.shahar91.curvetracker

import android.content.Context

@JvmInline
value class Dimension private constructor(private val px: Float) {

    companion object {
        fun dp(value: Float, context: Context) = Dimension(value * context.resources.displayMetrics.density)
    }

    fun asPx() = px
}