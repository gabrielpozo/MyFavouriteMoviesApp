package com.light.finder.extensions


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.R
import com.light.finder.ui.about.AboutFragment
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.camera.ModelStatus
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.lightfinder.CategoriesFragment
import com.light.finder.ui.lightfinder.DetailFragment


inline fun bundleFor(body: Bundle.() -> Unit): Bundle =
    Bundle().apply(body)


inline fun FirebaseAnalytics.logEventOnGoogleTagManager(
    typeEvent: String,
    body: Bundle.() -> Unit
) {
    logEvent(typeEvent, bundleFor(body))

}


inline fun AppEventsLogger.logEventOnFacebookSdk(
    typeEvent: String,
    body: Bundle.() -> Unit
) {
    logEvent(typeEvent, bundleFor(body))

}

//TODO extract this method in a standalone class
@Deprecated("will be removed in future releases", ReplaceWith("logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)"))
fun FirebaseAnalytics.trackScreen(
    currentFragment: Fragment?,
    activity: FragmentActivity?,
    tagCameraScreen: String? = null
) {
    activity?.let {
        if (tagCameraScreen == null) {
            when (currentFragment) {
                is DetailFragment -> {
                    setCurrentScreen(activity, activity.getString(R.string.product_details), null)
                }

                is CategoriesFragment -> {
                    setCurrentScreen(activity, activity.getString(R.string.product_results), null)
                }

                is CartFragment -> {
                    setCurrentScreen(activity, activity.getString(R.string.my_cart_fire), null)
                }
                is CameraFragment -> {
                    when {
                        currentFragment.getStatusView() == ModelStatus.FEED -> {
                            setCurrentScreen(
                                activity,
                                activity.getString(R.string.camera_feed),
                                null
                            )
                        }
                        currentFragment.getStatusView() == ModelStatus.LOADING -> {
                            setCurrentScreen(
                                activity,
                                activity.getString(R.string.camera_loading),
                                null
                            )
                        }
                        currentFragment.getStatusView() == ModelStatus.PERMISSION -> {
                            setCurrentScreen(
                                activity,
                                activity.getString(R.string.camera_permission),
                                null
                            )
                        }
                    }
                }

                is AboutFragment -> {
                    setCurrentScreen(activity, activity.getString(R.string.about_page), null)
                }
            }
        } else {
            if (currentFragment != null) {
                setCurrentScreen(activity, tagCameraScreen, null)
            }
        }
    }
}

