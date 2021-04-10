package com.shahar91.foodwatcher.ui.myDay.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class MyDayItemDecoration(context: Context, orientation: Int) : RecyclerView.ItemDecoration() {
    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL

        private const val TAG = "DividerItem"
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    private var mDivider: Drawable? = null

    /**
     * Current orientation. Either [.HORIZONTAL] or [.VERTICAL].
     */
    private var mOrientation = 0

    private val mBounds = Rect()

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        if (mDivider == null) {
            Log.w(
                TAG, "@android:attr/listDivider was not set in the theme used for this "
                        + "DividerItemDecoration. Please set that attribute all call setDrawable()"
            )
        }
        a.recycle()
        setOrientation(orientation)
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * [RecyclerView.LayoutManager] changes orientation.
     *
     * @param orientation [.HORIZONTAL] or [.VERTICAL]
     */
    fun setOrientation(orientation: Int) {
        require(!(orientation != HORIZONTAL && orientation != VERTICAL)) { "Invalid orientation. It should be either HORIZONTAL or VERTICAL" }
        mOrientation = orientation
    }

    /**
     * Sets the [Drawable] for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    fun setDrawable(drawable: Drawable?) {
        requireNotNull(drawable) { "Drawable cannot be null." }
        mDivider = drawable
    }

    /**
     * @return the [Drawable] for this divider.
     */
    fun getDrawable(): Drawable? {
        return mDivider
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || mDivider == null) {
            return
        }

        parent.adapter?.let { adapter ->
            parent.children.forEach { child ->
                val childAdapterPosition = parent.getChildAdapterPosition(child)
                    .let { if (it == RecyclerView.NO_POSITION) return else it }

                if (showDivider(adapter, parent.children.count(), childAdapterPosition)) {
                    if (mOrientation == VERTICAL) {
                        drawVertical(c, parent, child)
                    } else {
                        drawHorizontal(c, parent, child)
                    }
                }
            }
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView, child: View) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right, parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }

        parent.getDecoratedBoundsWithMargins(child, mBounds)
        val bottom = mBounds.bottom + child.translationY.roundToInt()
        val top = bottom - mDivider!!.intrinsicHeight
        mDivider!!.setBounds(left, top, right, bottom)
        mDivider!!.draw(canvas)
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView, child: View) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }

        parent.layoutManager?.getDecoratedBoundsWithMargins(child, mBounds)
        val right = mBounds.right + child.translationX.roundToInt()
        val left = right - mDivider!!.intrinsicWidth
        mDivider!!.setBounds(left, top, right, bottom)
        mDivider!!.draw(canvas)

        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (mDivider == null) {
            outRect[0, 0, 0] = 0
            return
        }
        parent.adapter?.let { adapter ->
            val childAdapterPosition = parent.getChildAdapterPosition(view)
                .let { if (it == RecyclerView.NO_POSITION) return else it }

            if (showDivider(adapter, parent.children.count(), childAdapterPosition)) {
                if (mOrientation == VERTICAL) {
                    outRect[0, 0, 0] = mDivider!!.intrinsicHeight
                } else {
                    outRect[0, 0, mDivider!!.intrinsicWidth] = 0
                }
            }
        }
    }

    private fun showDivider(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, childrenCount: Int, childAdapterPosition: Int): Boolean {
        return (childrenCount > (childAdapterPosition + 1) && adapter.getItemViewType(childAdapterPosition) == FoodEntryAdapter.ITEM_VIEW_TYPE_ITEM)
    }
}