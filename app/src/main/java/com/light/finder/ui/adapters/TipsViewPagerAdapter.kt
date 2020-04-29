package com.light.finder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.light.finder.R

class TipsViewPagerAdapter(private val context: Context) : PagerAdapter() {
    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val tips = Tips.values()[position]
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(tips.layoutResId, collection, false) as ViewGroup
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return Tips.values().size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}


enum class Tips(val layoutResId: Int) {
    FIRST(R.layout.item_tips_view_pager_first),
    SECOND(R.layout.item_tips_view_pager_second),
    THIRD(R.layout.item_tips_view_pager_third)
}