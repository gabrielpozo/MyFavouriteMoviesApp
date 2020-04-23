package com.light.finder.ui.terms

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.paris.extensions.style
import com.light.finder.CameraActivity
import com.light.finder.R
import com.light.finder.common.PrefManager
import com.light.finder.extensions.startActivity
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*

class TermsAndConditionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)


        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                buttonTerms.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryOnDark))
                buttonTerms.style(R.style.TermsButtonSelected)
                buttonTerms.isClickable = true
                buttonTerms.isFocusable = true
            } else {
                buttonTerms.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryDisabled))
                buttonTerms.style(R.style.TermsButtonUnSelected)
                buttonTerms.isClickable = false
                buttonTerms.isFocusable = false
            }
        }

        textViewStatement.setOnClickListener {
            goToPrivacyStatementActivity()
        }

        textViewTermsOfUse.setOnClickListener {
            goToTermsActivity()
        }


        buttonTerms.setOnClickListener {
            val prefManager = PrefManager(_context = this)
            prefManager.isTermsAccepted = true
            goToCameraActivity()
        }

    }

    private fun goToTermsActivity() {
        startActivity<TermsActivity> {}
        overrideAnimation()
    }

    private fun goToPrivacyStatementActivity() {
        startActivity<PrivacyStatementActivity>{}
        overrideAnimation()
    }

    private fun goToCameraActivity() {
        startActivity<CameraActivity>{}
        overrideAnimation()
        finish()
    }

    private fun overrideAnimation() = overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
}
