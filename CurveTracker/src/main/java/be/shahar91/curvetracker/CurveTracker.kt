package be.shahar91.curvetracker

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CurveTracker @JvmOverloads constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(ctx, attributeSet, defStyleAttr) {

    companion object {
        private const val ANIMATION_DURATION = 1000L
        private const val DEFAULT_MAX_VALUE = 100f
        private const val STROKE_WIDTH = 6f
    }

    private val arcRect = RectF()
    private val rect = Rect()
    private var diameter = 0f
    private var startAngle = 180f
    private var angle = 180f

    private var minValue: Float = 0f
    var ctMaxValue: Float = DEFAULT_MAX_VALUE
        set(value) {
            field = value
            drawMax()
            drawProgress()
        }
    var ctProgressValue: Float = 0f
        set(value) {
            field = value
            drawProgress()
        }

    private var animatorProgress: ValueAnimator? = null
    private var animationProgressValue = 0f
    private var animatorOverflow: ValueAnimator? = null
    private var animationOverflowValue = 0f
    private var animationMaxValue = 0f

    private var backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.on_floating_athens_gray)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = Dimension.dp(STROKE_WIDTH, context).asPx()
        isAntiAlias = true
    }

    private var progressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dodger_blue)
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        strokeWidth = Dimension.dp(STROKE_WIDTH, context).asPx()
        isAntiAlias = true
    }

    private var overflowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        strokeWidth = Dimension.dp(STROKE_WIDTH, context).asPx()
        isAntiAlias = true
    }

    private val minTextPaint = TextPaint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.dodger_blue)
        textSize = Dimension.dp(12f, context).asPx()
        textAlign = Paint.Align.LEFT
    }

    // TODO textAppearance on paint... https://stackoverflow.com/a/64423035/2263408
    private val maxTextPaint = TextPaint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.dodger_blue)
        textSize = Dimension.dp(12f, context).asPx()
        textAlign = Paint.Align.RIGHT
    }

    init {
        attributeSet?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.CurveTracker)
            ctMaxValue = a.getFloat(R.styleable.CurveTracker_ctMaxValue, DEFAULT_MAX_VALUE)
            ctProgressValue = a.getFloat(R.styleable.CurveTracker_ctProgressValue, 0f)

            a.recycle()
        }

        drawMax()
        drawProgress()

        if (isInEditMode) {
            animationMaxValue = 1f
            animationProgressValue = 1f
            animationOverflowValue = 1f
        }
    }

    private fun drawMax() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = ANIMATION_DURATION
            addUpdateListener {
                animationMaxValue = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun drawProgress() {
        animationOverflowValue = 0f
        animatorProgress?.cancel()
        animatorProgress = ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = ANIMATION_DURATION
            addUpdateListener {
                animationProgressValue = it.animatedValue as Float
                invalidate()
                if (it.animatedValue == 1F && ctProgressValue > ctMaxValue) {
                    drawOverflow()
                }
            }
            start()
        }
    }

    private fun drawOverflow() {
        animatorOverflow?.cancel()
        animatorOverflow = ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = ANIMATION_DURATION
            addUpdateListener {
                animationOverflowValue = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        updateRectForArc()
        canvas.drawArc(arcRect, startAngle, angle * animationMaxValue, false, backgroundPaint)
        canvas.drawArc(arcRect, startAngle, angle * (min(ctMaxValue, ctProgressValue) / ctMaxValue) * animationProgressValue, false, progressPaint)
        if (ctProgressValue > ctMaxValue) {
            canvas.drawArc(arcRect, 0f, -(angle * (ctProgressValue - ctMaxValue) / ctProgressValue * animationOverflowValue), false, overflowPaint)
        }

        drawMinText(canvas)
        drawMaxText(canvas)
    }

    private fun drawMaxText(canvas: Canvas) {
        maxTextPaint.getTextBounds(getMaxValueText(), 0, getMaxValueText().length, rect)

        val (_, ty) = getCoordinatesForText(6f)

        canvas.drawText(getMaxValueText(), arcRect.right + backgroundPaint.strokeWidth - paddingRight, ty, maxTextPaint)
    }

    private fun drawMinText(canvas: Canvas) {
        minTextPaint.getTextBounds(getMinValueText(), 0, getMinValueText().length, rect)

        val (_, ty) = getCoordinatesForText(18f)

        canvas.drawText(getMinValueText(), arcRect.left - backgroundPaint.strokeWidth + paddingLeft, ty, minTextPaint)
    }

    private fun getCoordinatesForText(angle: Float): Pair<Float, Float> {
        val circleCenterX = arcRect.right - (arcRect.width() / 2)
        val circleCenterY = arcRect.bottom - (arcRect.height() / 2)

        val angleFrom12 = angle / 24.0 * 2.0 * Math.PI
        val correctAngle = Math.PI / 2.0 - angleFrom12

        val tx = (circleCenterX + cos(correctAngle) * (diameter / 2))
        val ty = (circleCenterY - sin(correctAngle) * (diameter / 2)) + (rect.height() * 2)

//        val ty = ((arcRect.bottom - tyTemp) / 2) + tyTemp

        return tx.toFloat() to ty.toFloat()
    }

    private fun getMinValueText() = "$minValue"
    private fun getMaxValueText() = if (ctProgressValue < ctMaxValue) {
        "$ctMaxValue"
    } else {
        "$ctProgressValue"
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        diameter = min(width, height).toFloat()
        super.onSizeChanged(width, diameter.toInt(), oldWidth, oldHeight)

        invalidate()
    }

    private fun updateRectForArc() {
        val strokeWidth = backgroundPaint.strokeWidth

        val dirtyLeft = (width / 2) - (diameter / 2)
        val dirtyTop = (height / 2) - (diameter / 2)

        val left = dirtyLeft + strokeWidth + paddingLeft
        val top = dirtyTop + strokeWidth + paddingTop
        val right = dirtyLeft + diameter - strokeWidth - paddingRight
        val bottom = dirtyTop + diameter - strokeWidth - paddingBottom

        arcRect.set(left, top, right, bottom)
    }
}