package com.light.finder.ui.terms

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.paris.extensions.style
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.CameraActivity
import com.light.finder.R
import com.light.finder.common.PrefManager
import com.light.finder.extensions.gone
import com.light.finder.extensions.startActivity
import com.light.finder.extensions.visible
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*
import kotlinx.android.synthetic.main.layout_reusable_dialog.view.*

class TermsAndConditionsActivity : AppCompatActivity() {

    companion object {
        const val TERMS_URL =
            "https://www.signify.com/global/terms-of-use/mobile-apps/signify-lightfinder-en"
        const val PRIVACY_URL =
            "https://www.signify.com/global/privacy/legal-information/privacy-notice"
        const val NO_INTERNET_BANNER_DELAY = 5000L
    }

    private var prefManager: PrefManager? = null
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

        prefManager = PrefManager(this)
        switchConsent.isChecked = prefManager?.isConsentAccepted!!
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(switchConsent.isChecked)

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                buttonTerms.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryOnDark))
                buttonTerms.style(R.style.TermsButtonSelected)
                buttonTerms.isClickable = true
                buttonTerms.isFocusable = true
                setContinueClickListener()
            } else {
                buttonTerms.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryDisabled))
                buttonTerms.style(R.style.TermsButtonUnSelected)
                buttonTerms.isClickable = false
                buttonTerms.isFocusable = false
            }
        }

        textViewStatement.setOnClickListener {
            //todo no internet
            showErrorDialog(PRIVACY_URL)
        }

        textViewTermsOfUse.setOnClickListener {
            //todo no internet
            showErrorDialog(TERMS_URL)
        }


        switchConsent.setOnCheckedChangeListener { _, isChecked ->
            val prefManager = PrefManager(_context = this)
            prefManager.isConsentAccepted = isChecked
        }




    }


    private fun setContinueClickListener(){
        buttonTerms.setOnClickListener {
            val prefManager = PrefManager(_context = this)
            prefManager.isTermsAccepted = true
            goToCameraActivity()
        }
    }

    private fun openBrowser(URL: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(URL))
        startActivity(browserIntent)
    }

    private fun showErrorDialog(URL: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.layout_reusable_dialog, null)
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.window?.setDimAmount(0.6f)
        dialogView.textViewTitleDialog.text = getString(R.string.about_to_leave)
        dialogView.textViewSubTitleDialog.text = getString(R.string.will_be_opened)
        dialogView.buttonPositive.text = getString(R.string.ok)
        dialogView.buttonNeutral.text = getString(R.string.text_cancel)
        dialogView.buttonPositive.setOnClickListener {
            alertDialog.dismiss()
            openBrowser(URL)
        }
        dialogView.buttonNeutral.visible()
        dialogView.buttonNeutral.setOnClickListener {
            alertDialog.dismiss()
        }
        dialogView.buttonNegative.gone()
        alertDialog.show()

    }

    private fun goToCameraActivity() {
        startActivity<CameraActivity>{}
        overrideAnimation()
        finish()
    }

    private fun overrideAnimation() = overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
}
