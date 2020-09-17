package com.light.finder.ui.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.light.finder.BaseLightFinderActivity
import com.light.finder.CameraLightFinderActivity
import com.light.finder.R
import com.light.finder.SignifyApp
import com.light.finder.common.PrefManager
import com.light.finder.data.source.local.LocalKeyStoreImpl
import com.light.finder.data.source.local.LocalPreferenceDataSourceImpl
import com.light.finder.di.modules.submodules.SplashComponent
import com.light.finder.di.modules.submodules.SplashModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.finder.extensions.startActivity
import com.light.finder.ui.terms.TermsAndConditionsLightFinderActivity
import com.light.presentation.viewmodels.SplashState
import com.light.presentation.viewmodels.SplashViewModel
import com.light.source.local.LocalKeyStore
import com.light.source.local.LocalPreferenceDataSource


class SplashLightFinderActivity : BaseLightFinderActivity() {

    private var prefManager: PrefManager? = null
    private lateinit var component: SplashComponent
    private val splashViewModel: SplashViewModel by lazy { getViewModel { component.splashViewModel } }
    private val localKeyStore: LocalKeyStore by lazy {
        LocalKeyStoreImpl(
            SignifyApp.getContext()!!
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        prefManager = PrefManager(this)


        component = app.applicationComponent.plus(SplashModule())

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

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
        startActivity<TermsAndConditionsLightFinderActivity> { }
        finish()
    }

    private fun goToCameraActivity() {
        startActivity<CameraLightFinderActivity> { }
        finish()
    }
}
