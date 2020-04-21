package com.light.finder.ui.common


import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.viewpager.widget.ViewPager
import com.light.finder.R
import com.light.finder.extensions.tint


class DotsIndicator(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var viewPager: ViewPager? = null

    private var indicatorMargin = -1
    private var indicatorWidth = -1
    private var indicatorHeight = -1

    private var indicatorBackgroundResId: Int = 0
    private var indicatorUnselectedBackgroundResId: Int = 0

    private var lastPosition = -1

    private var backgroundResId: Int = 0
    private var unselectedBackgroundId: Int = 0
    private var dotTint: Int = 0

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.DotsIndicator)
        val intrinsicWidth: Int
        val intrinsicHeight: Int
        val intrinsicMargin: Int
        val intrinsicOrientation: Int
        val intrinsicGravity: Int

        try {
            intrinsicWidth = ta.getDimensionPixelSize(R.styleable.DotsIndicator_dot_width, -1)
            intrinsicHeight = ta.getDimensionPixelSize(R.styleable.DotsIndicator_dot_height, -1)
            intrinsicMargin = ta.getDimensionPixelSize(R.styleable.DotsIndicator_dot_margin, -1)
            intrinsicOrientation = ta.getInt(R.styleable.DotsIndicator_dots_orientation, -1)
            intrinsicGravity = ta.getInt(R.styleable.DotsIndicator_dots_gravity, -1)

            this.backgroundResId = ta.getResourceId(
                R.styleable.DotsIndicator_dot_drawable,
                R.drawable.dot
            )
            this.unselectedBackgroundId = ta.getResourceId(
                R.styleable.DotsIndicator_dot_drawable_unselected,
                this.backgroundResId
            )
            this.dotTint = ta.getColor(R.styleable.DotsIndicator_dot_tint, 0)
        } finally {
            ta.recycle()
        }

        indicatorWidth = intrinsicWidth
        indicatorHeight = intrinsicHeight
        indicatorMargin = intrinsicMargin

        indicatorBackgroundResId =
            if (this.backgroundResId == 0) R.drawable.dot else this.backgroundResId
        indicatorUnselectedBackgroundResId =
            if (this.unselectedBackgroundId == 0) this.backgroundResId else this.unselectedBackgroundId

        orientation =
            if (intrinsicOrientation == VERTICAL) VERTICAL else HORIZONTAL
        gravity =
            if (intrinsicGravity >= 0) intrinsicGravity else CENTER
    }

    fun setDotDrawable(
        @DrawableRes indicatorRes: Int,
        @DrawableRes unselectedIndicatorRes: Int = indicatorRes
    ) {
        this.backgroundResId = indicatorRes
        this.unselectedBackgroundId = unselectedIndicatorRes
        invalidateDots()
    }

    fun setDotTint(@ColorInt tint: Int) {
        this.dotTint = tint
        invalidateDots()
    }

    fun setDotTintRes(@ColorRes tintRes: Int) = setDotTint(getColor(context, tintRes))

    fun attachViewPager(viewPager: ViewPager?) {
        this.viewPager = viewPager
        this.viewPager?.let {
            if (it.adapter != null) {
                lastPosition = -1
                createIndicators()
                it.removeOnPageChangeListener(internalPageChangeListener)
                it.addOnPageChangeListener(internalPageChangeListener)
                internalPageChangeListener.onPageSelected(it.currentItem)
            }
        }
    }

    private fun invalidateDots() {
        for (i in 0 until childCount) {
            val indicator = getChildAt(i)
            val bgDrawableRes =
                if (currentItem() == i) indicatorBackgroundResId else indicatorUnselectedBackgroundResId
            var bgDrawable = getDrawable(context, bgDrawableRes)
            if (this.dotTint != 0) {
                bgDrawable = bgDrawable?.tint(this.dotTint)
            }
            indicator.background = bgDrawable
        }
    }

    private fun createIndicators(count: Int) {
        for (i in 0 until count) {
            val bgDrawable =
                if (currentItem() == i) indicatorBackgroundResId else indicatorUnselectedBackgroundResId
            addIndicator(
                orientation = orientation,
                drawableRes = bgDrawable
            )
        }
    }

    private fun internalPageSelected(position: Int) {
        val currentIndicator = if (lastPosition >= 0) getChildAt(lastPosition) else null
        if (currentIndicator != null) {
            currentIndicator.setBackgroundResource(indicatorUnselectedBackgroundResId)
        }
        val selectedIndicator = getChildAt(position)
        if (selectedIndicator != null) {
            selectedIndicator.setBackgroundResource(indicatorBackgroundResId)
        }
    }

    private fun createIndicators() {
        removeAllViews()
        val adapter = viewPager!!.adapter
        val count = adapter?.count ?: 0
        if (count <= 0) return
        createIndicators(count)
    }

    private fun addIndicator(
        orientation: Int,
        @DrawableRes drawableRes: Int
    ) {

        val indicator = View(context)

        var bgDrawable = getDrawable(context, drawableRes)
        if (this.dotTint != 0) {
            bgDrawable = bgDrawable?.tint(this.dotTint)
        }
        indicator.background = bgDrawable

        addView(indicator, indicatorWidth, indicatorHeight)
        val lp = indicator.layoutParams as LinearLayout.LayoutParams

        if (orientation == LinearLayout.HORIZONTAL) {
            lp.leftMargin = indicatorMargin
            lp.rightMargin = indicatorMargin
        } else {
            lp.topMargin = indicatorMargin
            lp.bottomMargin = indicatorMargin
        }

        indicator.layoutParams = lp
    }


    private fun currentItem() = viewPager?.currentItem ?: -1

    private val internalPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            if ((viewPager?.adapter?.count ?: 0) <= 0) return
            internalPageSelected(position)
            lastPosition = position
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) = Unit

        override fun onPageScrollStateChanged(state: Int) = Unit
    }

}