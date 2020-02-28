package com.light.finder

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color.parseColor
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.androidadvance.topsnackbar.TSnackbar
import com.light.finder.common.ConnectionLiveData
import com.light.finder.common.ConnectionModel
import com.light.finder.common.FragmentFrameHelper
import com.light.finder.common.FragmentFrameHelper.Companion.INDEX_CART
import com.light.finder.common.FragmentFrameHelper.Companion.INDEX_EXPERT
import com.light.finder.common.FragmentFrameHelper.Companion.INDEX_LIGHT_FINDER
import com.light.finder.extensions.newInstance
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.expert.ExpertFragment
import com.light.util.KEY_EVENT_ACTION
import com.light.util.KEY_EVENT_EXTRA
import com.ncapdevi.fragnav.FragNavController
import timber.log.Timber
import java.io.File


class CameraActivity : AppCompatActivity(), FragNavController.RootFragmentListener,
    BaseFragment.FragmentNavigation {
    private lateinit var container: FrameLayout

    private val fragmentHelper = FragmentFrameHelper(this)
    override val numberOfRootFragments: Int = 3
    private lateinit var snackBar: TSnackbar

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

        makeSnackBar()
        observeConnection()

    }

    private fun observeConnection() {
        val connectionLiveData = ConnectionLiveData(applicationContext)
        connectionLiveData.observe(this,
            Observer<ConnectionModel> { connection ->
                if (connection.isConnected) {
                    snackBar.dismiss()
                    when (connection.type) {
                        "WifiData" -> {
                            Timber.d("connected to wifi data")
                        }
                        "MobileData" -> {
                            Timber.d("connected to mobile data")
                        }
                    }
                } else {
                    snackBar.show()
                }
            })
    }

    private fun makeSnackBar() {
        snackBar = TSnackbar.make(container, "No internet connection", TSnackbar.LENGTH_INDEFINITE)
        snackBar.setActionTextColor(parseColor("#3c3c41"))
        val snackbarView = snackBar.view
        snackbarView.setBackgroundColor(parseColor("#d8d8d9"))
        val textView =
            snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        textView.setTextColor(parseColor("#3c3c41"))
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
