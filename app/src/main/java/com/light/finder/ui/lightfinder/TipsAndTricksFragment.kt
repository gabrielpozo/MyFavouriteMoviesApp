package com.light.finder.ui.lightfinder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.light.finder.R
import com.light.finder.common.VisibilityCallBack
import com.light.finder.common.WrappingViewPager
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.TipsViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_tips_and_tricks.*
import kotlin.math.abs


class TipsAndTricksFragment : BaseFragment() {

    companion object {
        const val REQUEST_CODE_TIPS = 1
    }

    private lateinit var visibilityCallBack: VisibilityCallBack

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tips_and_tricks, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            visibilityCallBack = context as VisibilityCallBack
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visibilityCallBack.onVisibilityChanged(true)
        initAdapters()
        setDoneClickListener()
    }

    private fun setDoneClickListener() {
        buttonGotIt.setOnClickListener {
            navigateBackToCamera()
        }
    }

    private fun initAdapters() {
        val viewPager = view?.findViewById<View>(R.id.viewPagerTips) as WrappingViewPager
        viewPager.adapter = TipsViewPagerAdapter(requireContext())
        viewPager.clipToPadding = false
        viewPager.setPadding(60, 20, 60, 20)
        viewPager.pageMargin = 24
        dots_indicator?.visibility = View.VISIBLE
        dots_indicator?.setViewPager(viewPager)
        setScrollInterceptor()
    }

    private fun setScrollInterceptor() {
        viewPagerTips.setOnTouchListener(object : View.OnTouchListener {

            var dragthreshold = 30
            var downX: Int = 0
            var downY: Int = 0

            override fun onTouch(v: View, event: MotionEvent): Boolean {

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downX = event.rawX.toInt()
                        downY = event.rawY.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val distanceX = abs(event.rawX.toInt() - downX)
                        val distanceY = abs(event.rawY.toInt() - downY)

                        if (distanceY > distanceX && distanceY > dragthreshold) {
                            viewPagerTips.parent.requestDisallowInterceptTouchEvent(false)
                            scrollViewTips.parent.requestDisallowInterceptTouchEvent(true)
                        } else if (distanceX > distanceY && distanceX > dragthreshold) {
                            viewPagerTips.parent.requestDisallowInterceptTouchEvent(true)
                            scrollViewTips.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        scrollViewTips.parent.requestDisallowInterceptTouchEvent(false)
                        viewPagerTips.parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
                return false
            }
        })
    }


    private fun navigateBackToCamera() {
        visibilityCallBack.onVisibilityChanged(false)
        mFragmentNavigation.popFragment()
    }

}