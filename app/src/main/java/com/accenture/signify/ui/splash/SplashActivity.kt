package com.accenture.signify.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.accenture.presentation.viewmodels.SplashState
import com.accenture.presentation.viewmodels.SplashViewModel
import com.accenture.signify.R
import com.accenture.signify.di.modules.SplashComponent
import com.accenture.signify.di.modules.SplashModule
import com.accenture.signify.extensions.app
import com.accenture.signify.extensions.getViewModel
import com.accenture.signify.CameraActivity

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
