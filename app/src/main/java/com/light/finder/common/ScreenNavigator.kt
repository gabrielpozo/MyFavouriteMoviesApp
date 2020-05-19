package com.light.finder.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.finder.CameraLightFinderActivity
import com.light.finder.R
import com.light.finder.UsabillaActivity
import com.light.finder.extensions.*
import com.light.finder.ui.about.AboutFragment
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.lightfinder.CategoriesFragment
import com.light.finder.ui.lightfinder.DetailFragment
import com.light.finder.ui.lightfinder.ProductVariationsLightFinderActivity
import com.light.finder.ui.lightfinder.TipsAndTricksLightFinderActivity
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavLogger
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import kotlinx.android.synthetic.main.activity_camera.*

class ScreenNavigator(private val activity: CameraLightFinderActivity) {
    companion object {
        const val INDEX_LIGHT_FINDER = FragNavController.TAB1
        const val INDEX_CART = FragNavController.TAB2
        const val INDEX_ABOUT = FragNavController.TAB3
    }

    private val fragNavController: FragNavController =
        FragNavController(activity.supportFragmentManager, R.id.fragment_container)
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(activity)


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
                    activity.bottom_navigation_view.currentItem = index
                }
            })
        }

        fragNavController.initialize(INDEX_LIGHT_FINDER, savedInstanceState)
        val initial = savedInstanceState == null
        if (initial) {
            activity.bottom_navigation_view.currentItem = INDEX_CART
        }


        activity.bottom_navigation_view.setOnTabSelectedListener { position, _ ->
            when (position) {
                INDEX_LIGHT_FINDER -> {
                    fragNavController.switchTab(INDEX_LIGHT_FINDER)
                    firebaseAnalytics.trackScreen(fragNavController.currentFrag, activity)
                }
                INDEX_CART -> {
                    fragNavController.switchTab(INDEX_CART)
                    reloadCartFragment()
                    firebaseAnalytics.trackScreen(fragNavController.currentFrag, activity)
                }
                INDEX_ABOUT -> {
                    fragNavController.switchTab(INDEX_ABOUT)
                    val current = fragNavController.currentFrag
                    if (current is AboutFragment) {
                        firebaseAnalytics.trackScreen(fragNavController.currentFrag, activity)
                        current.setLightStatusBar()
                    }
                }
            }
            true
        }
    }

    fun getCurrentFragment(): Fragment? = fragNavController.currentFrag


    fun onSaveInstanceState(outState: Bundle) {
        fragNavController.onSaveInstanceState(outState)
    }

    fun popFragmentNot(): Boolean {
        fragNavController.popFragment().not()
        firebaseAnalytics.trackScreen(fragNavController.currentFrag, activity)
        return false
    }

    private fun reloadCartFragment() {
        val current = fragNavController.currentFrag
        if (current is CartFragment) {
            current.onLoadWebView()
            current.onCheckIfOffline()
        }
    }


    fun navigateToVariationScreen(productList: List<Product>) {
        firebaseAnalytics.setCurrentScreen(
            activity,
            activity.getString(R.string.product_variations),
            null
        )
        activity.startActivityForResult<ProductVariationsLightFinderActivity> {
            putParcelableArrayListExtra(
                ProductVariationsLightFinderActivity.PRODUCTS_OPTIONS_ID_KEY,
                productList.parcelizeProductList()
            )
        }
        activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }

    fun navigateToTipsAndTricksScreen() {
        activity.startActivity<TipsAndTricksLightFinderActivity> {}
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.stay)
    }

    fun navigateToDetailScreen(category: Category) {
        fragNavController.pushFragment(DetailFragment.newInstance(category))
        firebaseAnalytics.trackScreen(fragNavController.currentFrag, activity)
    }

    fun navigateToCategoriesScreen(message: Message) {
        fragNavController.pushFragment(CategoriesFragment.newInstance(message))
        firebaseAnalytics.trackScreen(fragNavController.currentFrag, activity)
    }

    fun toCameraPermissionScreen(cameraFragment: CameraFragment) {
        firebaseAnalytics.trackScreen(
            cameraFragment,
            activity,
            activity.getString(R.string.camera_permission)
        )
    }

    fun toCameraFeedScreen(cameraFragment: CameraFragment) {
        firebaseAnalytics.trackScreen(
            cameraFragment,
            activity,
            activity.getString(R.string.camera_feed)
        )

    }

    fun toCameraLoading(cameraFragment: CameraFragment) {
        firebaseAnalytics.trackScreen(
            cameraFragment,
            activity,
            activity.getString(R.string.camera_loading)
        )
    }

    fun navigateToUsabillaForm() {
        activity.startActivity<UsabillaActivity> {
            this.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}