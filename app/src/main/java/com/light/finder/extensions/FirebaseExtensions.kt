package com.light.finder.extensions


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.R
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.camera.ModelStatus
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.lightfinder.CategoriesFragment
import com.light.finder.ui.lightfinder.DetailFragment
import kotlinx.android.synthetic.main.item_product.view.*


inline fun bundleFor(body: Bundle.() -> Unit): Bundle =
    Bundle().apply(body)


inline fun FirebaseAnalytics.logEventOnGoogleTagManager(
    typeEvent: String,
    body: Bundle.() -> Unit
) {
    logEvent(typeEvent, bundleFor(body))

}

fun FirebaseAnalytics.trackScreen(currentFragment: Fragment?, tag: String? = null) {
    if (tag == null) {
        when (currentFragment) {
            is DetailFragment -> {
                setCurrentScreen(currentFragment.requireActivity(), currentFragment.getString(R.string.product_details), null)
            }

            is CategoriesFragment -> {
                setCurrentScreen(currentFragment.requireActivity(), currentFragment.getString(R.string.product_results), null)
            }

            is CartFragment -> {
                setCurrentScreen(currentFragment.requireActivity(), currentFragment.getString(R.string.my_cart_fire), null)
            }
            is CameraFragment -> {
                when {
                    currentFragment.getStatusView() == ModelStatus.FEED -> {
                        setCurrentScreen(currentFragment.requireActivity(), currentFragment.getString(R.string.camera_feed), null)
                    }
                    currentFragment.getStatusView() == ModelStatus.LOADING -> {
                        setCurrentScreen(currentFragment.requireActivity(), currentFragment.getString(R.string.camera_loading), null)
                    }
                    currentFragment.getStatusView() == ModelStatus.PERMISSION -> {
                        setCurrentScreen(
                            currentFragment.requireActivity(),
                            currentFragment.getString(R.string.camera_permission),
                            null
                        )
                    }
                }
            }
        }
    } else {
        if (currentFragment != null) {
            setCurrentScreen(currentFragment.requireActivity(), tag, null)
        }
    }
}

