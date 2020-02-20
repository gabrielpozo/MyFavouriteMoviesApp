package com.light.finder.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.light.presentation.viewmodels.SplashState
import com.light.presentation.viewmodels.SplashViewModel
import com.light.finder.R
import com.light.finder.di.modules.SplashComponent
import com.light.finder.di.modules.SplashModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.finder.CameraActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var component: SplashComponent
    private val splashViewModel: SplashViewModel by lazy { getViewModel { component.splashViewModel } }

    override fun onCreate(savedInstanceState: Bundle?) {

        component = app.applicationComponent.plus(SplashModule())

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

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
        startActivity(Intent(this, CameraActivity::class.java))
    }
}
