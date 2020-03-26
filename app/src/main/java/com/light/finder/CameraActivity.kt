package com.light.finder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.light.finder.common.ConnectionLiveData
import com.light.finder.common.ConnectionModel
import com.light.finder.common.FragmentFrameHelper
import com.light.finder.common.FragmentFrameHelper.Companion.INDEX_CART
import com.light.finder.common.FragmentFrameHelper.Companion.INDEX_EXPERT
import com.light.finder.common.FragmentFrameHelper.Companion.INDEX_LIGHT_FINDER
import com.light.finder.common.VisibilityCallBack
import com.light.finder.extensions.gone
import com.light.finder.extensions.newInstance
import com.light.finder.extensions.visible
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.expert.ExpertFragment
import com.light.util.KEY_EVENT_ACTION
import com.light.util.KEY_EVENT_EXTRA
import com.ncapdevi.fragnav.FragNavController
import com.roughike.bottombar.BottomBarTab
import kotlinx.android.synthetic.main.activity_camera.*
import timber.log.Timber
import java.io.File


class CameraActivity : AppCompatActivity(), FragNavController.RootFragmentListener,
    BaseFragment.FragmentNavigation, VisibilityCallBack {

    private lateinit var container: FrameLayout
    private val fragmentHelper = FragmentFrameHelper(this)
    override val numberOfRootFragments: Int = 3

    override fun onVisibilityChanged(visible: Boolean) {
        if (visible) {
            bottom_navigation_view.gone()
        } else {
            bottom_navigation_view.visible()
        }
    }

    override fun onBadgeCountChanged(badgeCount: Int) {
        val cart: BottomBarTab = bottom_navigation_view.getTabWithId(R.id.cartFragment)
        cart.badgeBackgroundColor = getColor(R.color.informationDefault)
        cart.setBadgeCount(badgeCount)
    }

    override fun onCartCleared() {
        val cart: BottomBarTab = bottom_navigation_view.getTabWithId(R.id.cartFragment)
        cart.removeBadge()
    }

    override fun onBottomBarBlocked(isClickable: Boolean) {

        //TODO disable bottombar click
        
    }


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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        fragmentHelper.setupNavController(savedInstanceState)
        container = findViewById(R.id.fragment_container)


        observeConnection()

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
        fragmentHelper.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (fragmentHelper.popFragmentNot()) {
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

    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            INDEX_LIGHT_FINDER -> {
                return CameraFragment.newInstance()
            }
            INDEX_CART -> return CartFragment.newInstance()
            INDEX_EXPERT -> return ExpertFragment.newInstance()
        }
        throw IllegalStateException("Need to send an index that we know")
    }

    override fun pushFragment(fragment: Fragment, sharedElementList: List<Pair<View, String>>?) {
        fragmentHelper.pushFragment(fragment)
    }

}
