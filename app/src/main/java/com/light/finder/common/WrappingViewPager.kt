package com.light.finder.common

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager


class WrappingViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val firstChild = getChildAt(0)
        firstChild.measure(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(firstChild.measuredHeight, MeasureSpec.EXACTLY)
        )
    }
}