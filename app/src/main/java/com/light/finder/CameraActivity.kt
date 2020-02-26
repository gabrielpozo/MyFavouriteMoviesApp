package com.light.finder

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.light.finder.common.FragmentFrameHelper
import com.light.finder.common.FragmentFrameHelper.Companion.INDEX_CART
import com.light.finder.common.FragmentFrameHelper.Companion.INDEX_EXPERT
import com.light.finder.common.FragmentFrameHelper.Companion.INDEX_LIGHT_FINDER
import com.light.finder.extensions.FLAGS_FULLSCREEN
import com.light.finder.extensions.newInstance
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.expert.ExpertFragment
import com.light.finder.util.ConnectivityReceiver
import com.light.util.IMMERSIVE_FLAG_TIMEOUT
import com.light.util.KEY_EVENT_ACTION
import com.light.util.KEY_EVENT_EXTRA
import com.ncapdevi.fragnav.FragNavController
import timber.log.Timber
import java.io.File


class CameraActivity : AppCompatActivity(), FragNavController.RootFragmentListener,
    BaseFragment.FragmentNavigation, ConnectivityReceiver.ConnectivityReceiverListener {
    private lateinit var container: FrameLayout

    private val fragmentHelper = FragmentFrameHelper(this)
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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        fragmentHelper.setupNavController(savedInstanceState)
        container = findViewById(R.id.fragment_container)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
