package com.light.finder.ui.splash

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.light.finder.BaseActivity
import com.light.finder.CameraActivity
import com.light.finder.R
import com.light.finder.common.PrefManager
import com.light.finder.di.modules.SplashComponent
import com.light.finder.di.modules.SplashModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.finder.extensions.startActivity
import com.light.finder.ui.terms.TermsAndConditionsActivity
import com.light.presentation.viewmodels.SplashState
import com.light.presentation.viewmodels.SplashViewModel


class SplashActivity : BaseActivity() {

    private var prefManager: PrefManager? = null
    private lateinit var component: SplashComponent
    private val splashViewModel: SplashViewModel by lazy { getViewModel { component.splashViewModel } }

    override fun onCreate(savedInstanceState: Bundle?) {

        prefManager = PrefManager(this)


        component = app.applicationComponent.plus(SplashModule())

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel.liveData.observe(this, Observer {
            when (it) {
                is SplashState.CameraActivity -> {
                    if (prefManager?.isTermsAccepted!!) {
                        goToCameraActivity()
                    } else {
                        goToTermsAndConditionsActivity()
                    }
                }
            }
        })
    }

    private fun goToTermsAndConditionsActivity() {
        startActivity<TermsAndConditionsActivity> {  }
        finish()
    }

    private fun goToCameraActivity() {
        startActivity<CameraActivity> {  }
        finish()
    }
}
