package com.light.finder.ui.terms

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.light.finder.CameraActivity
import com.light.finder.R
import com.light.finder.di.modules.TermsComponent
import com.light.finder.di.modules.TermsModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.presentation.viewmodels.TermsViewModel
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*

class TermsAndConditionsActivity : AppCompatActivity() {

    private lateinit var component: TermsComponent
    private val termsViewModel: TermsViewModel by lazy { getViewModel { component.termsViewModel } }

    override fun onCreate(savedInstanceState: Bundle?) {

        component = app.applicationComponent.plus(TermsModule())
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)


        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                view.background = resources.getDrawable(R.drawable.button_curvy_corners,theme)
                buttonTerms.isClickable = true
                buttonTerms.isFocusable = true
            } else {
                view.background = resources.getDrawable(R.drawable.button_curvy_corners_border_disabled,theme)
                buttonTerms.isClickable = false
                buttonTerms.isFocusable = false
            }
        }


        buttonTerms.setOnClickListener {
            termsViewModel.onSharedPrefSaved(true)
            goToCameraActivity()
        }

    }

    private fun goToCameraActivity() {
        startActivity(Intent(this, CameraActivity::class.java))
        finish()
    }
}
