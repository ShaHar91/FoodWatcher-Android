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

//TODO: add text in the middle that says how many points are remaining, or 0 if crossed the max
//TODO: add line at the end of the overflow arc
//TODO: Provide other colors for the tracks and the texts
//TODO: add a text at the bottom in the center that says 'Daily remaining'
//TODO: make the overflow optional?? 🤔
//TODO: make the max/progress text animated (counting up)?? 🤔
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

    // <editor-fold desc="attributes">
    var ctProgressValue: Float = 0f
        set(value) {
            field = value
            animateProgress()
        }

    var ctMaxValue: Float = DEFAULT_MAX_VALUE
        set(value) {
            field = value
            animateMax()
            animateProgress()
        }

    private val minValueText: String
        get() = "0"
    private val maxValueText: String
        get() = String.format("%.0f", if (ctProgressValue < ctMaxValue) ctMaxValue else ctProgressValue)
    // </editor-fold>

    private val arcRect = RectF()
    private val rect = Rect()
    private var diameter = 0f
    private val radius: Float
        get() = diameter / 2
    private val arcStrokeWidth: Float
        get() = Dimension.dp(STROKE_WIDTH, context).asPx()

    /**
     * The starting angle to draw the arc at. The 0° angle for Android is at the right (instead at the top)
     */
    private var startAngle = 180f

    /**
     * The sweepAngle to the draw the arc to. This will technically be added to the [startAngle] to draw the full arc.
     * Using a positive number will draw it clockwise, using a negative number will be counterclockwise
     */
    private var angle = 180f

    // <editor-fold desc="Animations">
    private var animatorProgress: ValueAnimator? = null
    private var animationProgressValue = 0f
    private var animatorOverflow: ValueAnimator? = null
    private var animationOverflowValue = 0f
    private var animationMaxValue = 0f

    /**
     * Will only be called once when this view is first created.
     * Using [animateProgress] right after this, will give a nice starting animation
     */
    private fun animateMax() {
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

    /**
     * Will be called anytime the [ctProgressValue] changes.
     * Any previous running animation would need to be cancelled before starting a new one.
     */
    private fun animateProgress() {
        animationOverflowValue = 0f
        animatorProgress?.cancel()
        animatorProgress = ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = if (ctProgressValue > ctMaxValue) 350 else ANIMATION_DURATION
            addUpdateListener {
                animationProgressValue = it.animatedValue as Float
                invalidate()
                if (it.animatedValue == 1F && ctProgressValue > ctMaxValue) {
                    animateOverflow()
                }
            }
            start()
        }
    }

    /**
     * Will be called when the [animateProgress] animation has ended AND the [ctProgressValue] is higher than the [ctMaxValue].
     * Any previous running animation would need to be cancelled before starting a new one.
     */
    private fun animateOverflow() {
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
    // </editor-fold>

    // <editor-fold desc="Paints">
    private var backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.on_floating_athens_gray)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = arcStrokeWidth
        isAntiAlias = true
    }

    private var progressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dodger_blue)
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        strokeWidth = arcStrokeWidth
        isAntiAlias = true
    }

    private var overflowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        strokeWidth = arcStrokeWidth
        isAntiAlias = true
    }

    // TODO textAppearance on paint... https://stackoverflow.com/a/64423035/2263408
    private val maxTextPaint = TextPaint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.dodger_blue)
        textSize = Dimension.dp(12f, context).asPx()
        textAlign = Paint.Align.RIGHT
    }

    private val minTextPaint = TextPaint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.dodger_blue)
        textSize = Dimension.dp(12f, context).asPx()
        textAlign = Paint.Align.LEFT
    }
    // </editor-fold>

    init {
        attributeSet?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.CurveTracker)
            ctMaxValue = a.getFloat(R.styleable.CurveTracker_ctMaxValue, DEFAULT_MAX_VALUE)
            ctProgressValue = a.getFloat(R.styleable.CurveTracker_ctProgressValue, 0f)

            a.recycle()
        }

        animateProgress()
        animateMax()

        if (isInEditMode) {
            animationMaxValue = 1f
            animationProgressValue = 1f
            animationOverflowValue = 1f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        updateRectForArc()

        // Draw background
        canvas.drawArc(arcRect, startAngle, angle * animationMaxValue, false, backgroundPaint)
        // Draw progress
        canvas.drawArc(arcRect, startAngle, angle * (min(ctMaxValue, ctProgressValue) / ctMaxValue) * animationProgressValue, false, progressPaint)
        // Draw overflow
        if (ctProgressValue > ctMaxValue) {
            canvas.drawArc(arcRect, 0f, -(angle * (ctProgressValue - ctMaxValue) / ctProgressValue * animationOverflowValue), false, overflowPaint)
        }

        drawMinText(canvas)
        drawMaxText(canvas)
    }

    private fun drawMaxText(canvas: Canvas) {
        maxTextPaint.getTextBounds(maxValueText, 0, maxValueText.length, rect)

        val (_, ty) = getCoordinatesForText(6f)

        canvas.drawText(maxValueText, arcRect.right + (backgroundPaint.strokeWidth / 2) - paddingRight, ty, maxTextPaint)
    }

    private fun drawMinText(canvas: Canvas) {
        minTextPaint.getTextBounds(minValueText, 0, minValueText.length, rect)

        val (_, ty) = getCoordinatesForText(18f)

        canvas.drawText(minValueText, arcRect.left - (backgroundPaint.strokeWidth / 2) + paddingLeft, ty, minTextPaint)
    }

    private fun getCoordinatesForText(angle: Float): Pair<Float, Float> {
        val circleCenterX = arcRect.right - (arcRect.width() / 2)
        val circleCenterY = arcRect.bottom - (arcRect.height() / 2)

        val angleFrom12 = angle / 24.0 * 2.0 * Math.PI
        val correctAngle = Math.PI / 2.0 - angleFrom12

        val tx = (circleCenterX + cos(correctAngle) * radius)
        val ty = (circleCenterY - sin(correctAngle) * radius) + (rect.height() * 2)

//        val ty = ((arcRect.bottom - tyTemp) / 2) + tyTemp

        return tx.toFloat() to ty.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val smallerDimens = min(width, height)

        //TODO: "+100" should be a calculation of the textHeight of the progressTextView and the middleBottomTextView
        setMeasuredDimension(smallerDimens, (smallerDimens / 2) + 100)

        diameter = smallerDimens.toFloat()

        val left = 0 + arcStrokeWidth + paddingLeft
        val top = 0 + arcStrokeWidth + paddingTop
        val right = smallerDimens - arcStrokeWidth - paddingRight
        val bottom = smallerDimens - arcStrokeWidth - paddingBottom

        arcRect.set(left, top, right, bottom)
    }
//
//    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
//        diameter = min(width, height).toFloat()
//        super.onSizeChanged(width, diameter.toInt(), oldWidth, oldHeight)
//
//        invalidate()
//    }
}