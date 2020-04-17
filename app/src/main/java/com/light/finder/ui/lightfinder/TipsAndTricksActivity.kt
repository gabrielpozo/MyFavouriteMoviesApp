package com.light.finder.ui.lightfinder

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.light.finder.R
import com.light.finder.common.VisibilityCallBack
import com.light.finder.common.WrappingViewPager
import com.light.finder.ui.adapters.TipsViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_tips_and_tricks.*
import kotlin.math.abs


class TipsAndTricksActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_TIPS = 1
    }

    private lateinit var visibilityCallBack: VisibilityCallBack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
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
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }

}