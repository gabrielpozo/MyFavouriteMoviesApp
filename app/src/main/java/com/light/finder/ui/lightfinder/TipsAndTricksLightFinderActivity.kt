package com.light.finder.ui.lightfinder

import android.os.Bundle
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.BaseLightFinderActivity
import com.light.finder.R
import com.light.finder.common.WrappingViewPager
import com.light.finder.ui.adapters.TipsViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_tips_and_tricks.*


class TipsAndTricksLightFinderActivity : BaseLightFinderActivity() {

    companion object {
        const val REQUEST_CODE_TIPS = 1
    }

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_tips_and_tricks)

        initAdapters()
        setDoneClickListener()
    }


    private fun setDoneClickListener() {
        buttonGotIt.setOnClickListener {
            navigateBackToCamera()
        }
    }

    private fun initAdapters() {
        val viewPager = findViewById<View>(R.id.viewPagerTips) as WrappingViewPager
        viewPager.adapter = TipsViewPagerAdapter(this)
        viewPager.clipToPadding = false
        viewPager.setPadding(60, 20, 60, 20)
        viewPager.pageMargin = 24
        dots_indicator?.visibility = View.VISIBLE
        dots_indicator?.attachViewPager(viewPager)

    }


    private fun navigateBackToCamera() {
        finish()
        setAnimation()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setAnimation()
    }

    private fun setAnimation() {
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
    }

}