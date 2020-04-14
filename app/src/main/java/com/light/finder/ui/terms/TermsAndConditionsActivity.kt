package com.light.finder.ui.terms

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.light.finder.CameraActivity
import com.light.finder.R

class TermsAndConditionsActivity : AppCompatActivity() {

    //private lateinit var component: SplashComponent
    //private val splashViewModel: SplashViewModel by lazy { getViewModel { component.splashViewModel } }

    override fun onCreate(savedInstanceState: Bundle?) {

        //component = app.applicationComponent.plus(SplashModule())
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

    }

    private fun goToCameraActivity() {
        startActivity(Intent(this, CameraActivity::class.java))
        finish()
    }
}
