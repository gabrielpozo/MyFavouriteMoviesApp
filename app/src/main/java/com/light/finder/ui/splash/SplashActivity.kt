package com.light.finder.ui.splash

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.light.finder.CameraActivity
import com.light.finder.R
import com.light.finder.di.modules.SplashComponent
import com.light.finder.di.modules.SplashModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.presentation.viewmodels.SplashState
import com.light.presentation.viewmodels.SplashViewModel


class SplashActivity : AppCompatActivity() {

    private lateinit var component: SplashComponent
    private val splashViewModel: SplashViewModel by lazy { getViewModel { component.splashViewModel } }

    override fun onCreate(savedInstanceState: Bundle?) {

        component = app.applicationComponent.plus(SplashModule())
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel.liveData.observe(this, Observer {
            when (it) {
                is SplashState.CameraActivity -> {
                    goToCameraActivity()
                }
            }
        })
    }

    private fun goToCameraActivity() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(Intent(this, CameraActivity::class.java))
    }
}
