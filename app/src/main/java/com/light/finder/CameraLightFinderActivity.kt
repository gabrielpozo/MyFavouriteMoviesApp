package com.light.finder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.aurelhubert.ahbottomnavigation.notification.AHNotification
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.common.*
import com.light.finder.common.ScreenNavigator.Companion.INDEX_ABOUT
import com.light.finder.common.ScreenNavigator.Companion.INDEX_CART
import com.light.finder.common.ScreenNavigator.Companion.INDEX_LIGHT_FINDER
import com.light.finder.di.modules.camera.LightFinderComponent
import com.light.finder.di.modules.camera.LightFinderModule
import com.light.finder.extensions.*
import com.light.finder.ui.about.AboutFragment
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.camera.ModelStatus
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.lightfinder.DetailFragment
import com.light.finder.ui.liveambiance.LiveAmbianceLightFinderActivity
import com.light.util.KEY_EVENT_ACTION
import com.light.util.KEY_EVENT_EXTRA
import com.ncapdevi.fragnav.FragNavController
import kotlinx.android.synthetic.main.activity_camera.*
import timber.log.Timber
import java.io.File


class CameraLightFinderActivity : BaseLightFinderActivity(), FragNavController.RootFragmentListener,
    ActivityCallback, ReloadingCallback {


    private lateinit var container: FrameLayout
    private val screenNavigator: ScreenNavigator by lazy { lightFinderComponent.screenNavigator }
    val lightFinderComponent: LightFinderComponent by lazy {
        app.applicationComponent.plus(
            LightFinderModule(
                this
            )
        )
    }
    private var isBackButtonBlocked = false
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override val numberOfRootFragments: Int = 3
    private var carToReload = false

    companion object {
        const val LIMITED_NUMBER_BADGE = 100
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setContentView(R.layout.activity_camera)
        screenNavigator.setupNavController(savedInstanceState)
        container = findViewById(R.id.fragment_container)

        setBottomBar()

        observeConnection()
    }


    override fun setBottomBarInvisibility(invisible: Boolean) {
        if (invisible) {
            onBottomBarBlocked(false)
            bottom_navigation_view.gone()
        } else {
            onBottomBarBlocked(true)
            bottom_navigation_view.visible()
        }
    }

    override fun onBadgeCountChanged(badgeCount: Int) {
        val textCount = if (badgeCount < LIMITED_NUMBER_BADGE) {
            badgeCount.toString()
        } else {
            getString(R.string.badge_plus_100)
        }
        val notification: AHNotification = AHNotification.Builder().setText(textCount)
            .setBackgroundColor(getColor(R.color.informationDefault)).build()


        bottom_navigation_view.setNotification(notification, INDEX_CART)
    }

    override fun onCartCleared() {
        bottom_navigation_view.setNotification(AHNotification(), INDEX_CART)

    }

    override fun onBottomBarBlocked(isClickable: Boolean) {
        if (!isClickable) {
            isBackButtonBlocked = true
            bottom_navigation_view.setItemDisableColor(getColor(R.color.backgroundLight))
            bottom_navigation_view.disableItemAtPosition(INDEX_CART)
            bottom_navigation_view.disableItemAtPosition(INDEX_ABOUT)
        } else {
            isBackButtonBlocked = false
            bottom_navigation_view.enableItemAtPosition(INDEX_CART)
            bottom_navigation_view.enableItemAtPosition(INDEX_ABOUT)
        }
    }

    override fun onInternetConnectionLost() {
        firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.no_internet_banner)) {
        }
        no_internet_banner?.slideVertically(0F)
        Handler().postDelayed({
            no_internet_banner.slideVertically(-no_internet_banner.height.toFloat())
        }, 5000)
    }

    override fun setCurrentlyReloaded(reloadCart: Boolean) {
        carToReload = reloadCart
    }

    override fun hasBeenReload(): Boolean = carToReload

    private fun setBottomBar() {
        val navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu)
        navigationAdapter.setupWithBottomNavigation(bottom_navigation_view)
        // Set background color
        bottom_navigation_view.defaultBackgroundColor = getColor(R.color.backgroundDark)
        bottom_navigation_view.accentColor = getColor(R.color.primaryOnDark)
        bottom_navigation_view.inactiveColor = getColor(R.color.backgroundLight)

        val face = ResourcesCompat.getFont(this, R.font.bold)
        bottom_navigation_view.setTitleTextSize(
            resources.getDimension(R.dimen.active_tab),
            resources.getDimension(R.dimen.inactive_tab)
        )
        bottom_navigation_view.setPadding(0, 20, 0, 0)

        bottom_navigation_view.setTitleTypeface(face)
    }

    private fun observeConnection() {
        //todo move to viewmodel
        val connectionLiveData = ConnectionLiveData(applicationContext)
        connectionLiveData.observe(this,
            Observer<ConnectionModel> { connection ->
                if (connection.isConnected) {
                    // snackBar.dismiss()
                    when (connection.type) {
                        "WifiData" -> {
                            Timber.d("connected to wifi data")
                        }
                        "MobileData" -> {
                            Timber.d("connected to mobile data")
                        }
                    }
                } else {
                    // snackBar.show()
                    Timber.d("no connection")
                }
            })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        screenNavigator.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        val current = screenNavigator.getCurrentFragment()
        if (current is CameraFragment) {
            if (current.getStatusView() == ModelStatus.GALLERY) {
                // go back to gallery
                current.pickImageFromGallery()

                return
            }
        }


        if (!isBackButtonBlocked && screenNavigator.popFragmentNot()) {
            super.onBackPressed()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val op = data?.getIntExtra(
            LiveAmbianceLightFinderActivity.CCT_LIST_EXTRA,
            0
        ) ?: -1
        if (requestCode == LiveAmbianceLightFinderActivity.REQUEST_CODE_AMBIANCE) {
            val currentFragment = screenNavigator.getCurrentFragment()
            if (currentFragment is DetailFragment) {
                currentFragment.returningFromLiveAmbiance(
                    data?.getIntExtra(
                        LiveAmbianceLightFinderActivity.CCT_LIST_EXTRA,
                        0
                    ) ?: -1
                )
            }
        }
    }

    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            INDEX_LIGHT_FINDER -> {
                return CameraFragment.newInstance()
            }
            INDEX_CART -> return CartFragment.newInstance()
            INDEX_ABOUT -> return AboutFragment.newInstance()
        }
        throw IllegalStateException("Need to send an index that we know")
    }

}
