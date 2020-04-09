package com.light.finder.common

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.light.finder.CameraActivity
import com.light.finder.R
import com.light.finder.ui.cart.CartFragment
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavLogger
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import kotlinx.android.synthetic.main.activity_camera.*

class FragmentFrameHelper(private val activity: CameraActivity) {
    companion object {
        const val INDEX_LIGHT_FINDER = FragNavController.TAB1
        const val INDEX_CART = FragNavController.TAB2
        const val INDEX_EXPERT = FragNavController.TAB3
    }

    private val fragNavController: FragNavController =
        FragNavController(activity.supportFragmentManager, R.id.fragment_container)


    fun setupNavController(savedInstanceState: Bundle?) {
        fragNavController.apply {
            rootFragmentListener = activity
            createEager = true
            fragNavLogger = object : FragNavLogger {
                override fun error(message: String, throwable: Throwable) {
                }
            }

            defaultTransactionOptions = FragNavTransactionOptions.newBuilder().customAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            ).build()
            fragmentHideStrategy = FragNavController.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH

            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    Log.d("GabrielBottom", "switch Tabs")
                    activity.bottom_navigation_view.currentItem = index
                }
            })
        }

        fragNavController.initialize(INDEX_LIGHT_FINDER, savedInstanceState)
        val initial = savedInstanceState == null
        if (initial) {
            activity.bottom_navigation_view.currentItem = INDEX_CART
        }


        activity.bottom_navigation_view.setOnTabSelectedListener { position, wasSelected ->

            when (position) {
                INDEX_LIGHT_FINDER -> fragNavController.switchTab(INDEX_LIGHT_FINDER)
                INDEX_CART -> {
                    fragNavController.switchTab(INDEX_CART)
                    val current = fragNavController.currentFrag
                    if (current is CartFragment) {
                        current.reloadWebView()
                    }
                }
                INDEX_EXPERT -> fragNavController.switchTab(INDEX_EXPERT)
            }
            true
        }
    }

    fun getCurrentFragment(): Fragment? = fragNavController.currentFrag


    fun pushFragment(fragment: Fragment) {
        fragNavController.pushFragment(fragment)
    }

    fun onSaveInstanceState(outState: Bundle) {
        fragNavController.onSaveInstanceState(outState)
    }

    fun popFragmentNot(): Boolean = fragNavController.popFragment().not()

}