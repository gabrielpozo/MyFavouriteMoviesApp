package com.light.finder

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.RoomOpenHelper
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.light.finder.extensions.FLAGS_FULLSCREEN
import com.light.finder.extensions.newInstance
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.camera.PermissionsFragment
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.expert.ExpertFragment
import com.light.finder.util.ConnectivityReceiver
import com.light.util.IMMERSIVE_FLAG_TIMEOUT
import com.light.util.KEY_EVENT_ACTION
import com.light.util.KEY_EVENT_EXTRA
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavLogger
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import kotlinx.android.synthetic.main.activity_camera.*
import timber.log.Timber
import java.io.File


/**
 * Main entry point into our app. This app follows the single-activity pattern, and all
 * functionality is implemented in the form of fragments.
 */
class CameraActivity : AppCompatActivity(), FragNavController.RootFragmentListener,
    BaseFragment.FragmentNavigation, ConnectivityReceiver.ConnectivityReceiverListener {
    private lateinit var container: FrameLayout
    private var snackBarView : Snackbar? = null

    private val fragNavController: FragNavController =
        FragNavController(supportFragmentManager, R.id.fragment_container)
    override val numberOfRootFragments: Int = 3

    companion object {
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
        setContentView(R.layout.activity_camera)

        /**
         *ON CREATE
         */

        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )


        fragNavController.apply {
            rootFragmentListener = this@CameraActivity
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
                    bottom_navigation_view.selectTabAtPosition(index)
                }
            })
        }

        fragNavController.initialize(INDEX_LIGHT_FINDER, savedInstanceState)
        val initial = savedInstanceState == null
        if (initial) {
            bottom_navigation_view.selectTabAtPosition(INDEX_LIGHT_FINDER)
        }

        bottom_navigation_view.setOnTabSelectListener({ tabId ->
            when (tabId) {
                R.id.camera_fragment -> fragNavController.switchTab(INDEX_LIGHT_FINDER)
                R.id.cartFragment -> fragNavController.switchTab(INDEX_CART)
                R.id.expertFragment -> fragNavController.switchTab(INDEX_EXPERT)
            }
        }, initial)

        bottom_navigation_view.setOnTabReselectListener { fragNavController.clearStack() }

        container = findViewById(R.id.fragment_container)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (fragNavController.popFragment().not()) {
            super.onBackPressed()
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (isConnected) {
            //todo show snackbar from the top
            Timber.d("EGE IS CONNECTED")
        } else {
            Timber.d("EGE IS NOT CONNECTED")
        }
    }


    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
        container.postDelayed({
            container.systemUiVisibility = FLAGS_FULLSCREEN
        }, IMMERSIVE_FLAG_TIMEOUT)
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

    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            INDEX_LIGHT_FINDER -> {
                return PermissionsFragment.newInstance()
            }
            INDEX_CART -> return CartFragment.newInstance()
            INDEX_EXPERT -> return ExpertFragment.newInstance()
        }
        throw IllegalStateException("Need to send an index that we know")
    }

    override fun pushFragment(fragment: Fragment, sharedElementList: List<Pair<View, String>>?) {
        fragNavController.pushFragment(fragment)
    }

}

const val INDEX_LIGHT_FINDER = FragNavController.TAB1
const val INDEX_CART = FragNavController.TAB2
const val INDEX_EXPERT = FragNavController.TAB3