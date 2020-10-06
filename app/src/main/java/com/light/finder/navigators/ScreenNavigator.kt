package com.light.finder.navigators

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.domain.model.Category
import com.light.domain.model.CctType
import com.light.domain.model.Message
import com.light.finder.CameraLightFinderActivity
import com.light.finder.R
import com.light.finder.UsabillaActivity
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.about.AboutFragment
import com.light.finder.ui.browse.BrowseActivity
import com.light.finder.ui.browse.BrowseActivity.Companion.REQUEST_CODE_BROWSING
import com.light.finder.ui.browse.BrowseResultFragment
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.lightfinder.CategoriesFragment
import com.light.finder.ui.lightfinder.DetailFragment
import com.light.finder.ui.lightfinder.TipsAndTricksLightFinderActivity
import com.light.finder.ui.liveambiance.LiveAmbianceLightFinderActivity
import com.light.finder.ui.liveambiance.LiveAmbianceLightFinderActivity.Companion.REQUEST_CODE_AMBIANCE
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
    private lateinit var rootFragment: BaseFragment


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
            fragmentHideStrategy = FragNavController.HIDE

            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    activity.bottom_navigation_view.currentItem = index
                }
            })
        }

        fragNavController.initialize(INDEX_LIGHT_FINDER, savedInstanceState)
        val initial = savedInstanceState == null
        if (initial) {
            activity.bottom_navigation_view.currentItem =
                INDEX_CART
        }

        fun onLightFinderTabPressed(current: Fragment?, wasSelected: Boolean) {

            if (!wasSelected) {
                return
            }

            when (current) {
                is BrowseResultFragment, is CategoriesFragment, is DetailFragment
                -> {
                    activity.bottom_navigation_view.setItemDisableColor(getColor(activity, R.color.backgroundLight))
                    activity.bottom_navigation_view.disableItemAtPosition(INDEX_LIGHT_FINDER)
                    goToHomeScreen()
                }
            }
        }

        activity.bottom_navigation_view.setOnTabSelectedListener { position, wasSelected ->
            when (position) {
                INDEX_LIGHT_FINDER -> {
                    fragNavController.switchTab(INDEX_LIGHT_FINDER)
                    //TODO("create show/navigate event for every single fragment -> BaseFragment")
                    val current = getCurrentFragment()

                    onLightFinderTabPressed(current, wasSelected)

                    if (current is CameraFragment) {
                        current.enableCameraCaptureButton()
                    }
                    //TODO("create show/navigate event for every single fragment -> BaseFragment")
                    firebaseAnalytics.trackScreen(fragNavController.currentFrag, activity)
                }
                INDEX_CART -> {
                    val current = getCurrentFragment()
                    if (current is CameraFragment) {
                        current.disableCameraCaptureButton()
                    }
                    fragNavController.switchTab(INDEX_CART)
                    reloadCartFragment()
                    //TODO("create show/navigate event for every single fragment -> BaseFragment")
                    firebaseAnalytics.trackScreen(fragNavController.currentFrag, activity)
                }
                INDEX_ABOUT -> {
                    //TODO("create show/navigate event for every single fragment -> BaseFragment")
                    val current = getCurrentFragment()
                    if (current is CameraFragment) {
                        current.disableCameraCaptureButton()
                    }

                    fragNavController.switchTab(INDEX_ABOUT)
                    if (getCurrentFragment() is AboutFragment) {
                        firebaseAnalytics.trackScreen(fragNavController.currentFrag, activity)
                        (getCurrentFragment() as AboutFragment).setLightStatusBar()
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

    fun setInitialRootFragment(fragment: BaseFragment) {
        rootFragment = fragment
    }

    fun getInitialRootFragment(): BaseFragment = rootFragment


    fun popFragmentNot(): Boolean {
        val isFragmentPopped = fragNavController.popFragment().not()
        val current = getCurrentFragment()
        if (current is CameraFragment) {
            current.restoreCameraFromScanning()
        }
        //TODO("create show/navigate event for every single fragment -> BaseFragment")
        firebaseAnalytics.trackScreen(fragNavController.currentFrag, activity)
        return isFragmentPopped
    }

    private fun reloadCartFragment() {
        val current = fragNavController.currentFrag
        if (current is CartFragment) {
            current.onLoadWebView()
            current.onCheckIfOffline()
        }
    }

    private fun goToHomeScreen() {
        activity.startActivity<CameraLightFinderActivity> {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    fun navigateToSettings() {
        activity.startActivity(Intent(Settings.ACTION_SETTINGS))
    }

    fun navigateToLiveAmbiance(listCctType: List<CctType>) {
        activity.startActivityForResult<LiveAmbianceLightFinderActivity>(REQUEST_CODE_AMBIANCE) {
            putParcelableArrayListExtra(
                LiveAmbianceLightFinderActivity.LIVE_AMBIANCE_ID_KEY,
                listCctType.parcelizeCctList()
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

    fun toGalleryPreview(cameraFragment: CameraFragment) {
        firebaseAnalytics.trackScreen(
            cameraFragment,
            activity,
            activity.getString(R.string.gallery_preview)
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

    fun navigateToBrowsingFiltering() {
        activity.startActivityForResult<BrowseActivity>(REQUEST_CODE_BROWSING) {}
        activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }
}