package com.light.finder.ui.terms

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.airbnb.paris.extensions.style
import com.facebook.FacebookSdk
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.BaseLightFinderActivity
import com.light.finder.CameraLightFinderActivity
import com.light.finder.R
import com.light.finder.common.ConnectivityRequester
import com.light.finder.common.InternetUtil
import com.light.finder.common.PrefManager
import com.light.finder.di.modules.submodules.TermsComponent
import com.light.finder.di.modules.submodules.TermsModule
import com.light.finder.extensions.*
import com.light.presentation.viewmodels.TermsViewModel
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*
import kotlinx.android.synthetic.main.layout_reusable_dialog.view.*
import timber.log.Timber

class TermsAndConditionsLightFinderActivity : BaseLightFinderActivity() {

    companion object {
        const val TERMS_URL =
            "https://www.signify.com/global/terms-of-use/mobile-apps/signify-lightfinder-en"
        const val PRIVACY_URL =
            "https://www.signify.com/global/privacy/legal-information/privacy-notice"
        const val COOKIES_NOTICE_URL =
            "https://www.signify.com/global/privacy/legal-information/cookies-notice"
        const val NO_INTERNET_BANNER_DELAY = 5000L
    }

    private var prefManager: PrefManager? = null
    private lateinit var component: TermsComponent
    private val viewModel: TermsViewModel by lazy { getViewModel { component.termsViewModel } }
    private lateinit var connectivityRequester: ConnectivityRequester

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

        component = app.applicationComponent.plus(TermsModule())
        connectivityRequester = ConnectivityRequester(this)

        prefManager = PrefManager(this)
        switchConsent.isChecked = prefManager?.isConsentAccepted!!

        setFacebookConsent(switchConsent.isChecked )

        if (!InternetUtil.isInternetOn()) {
            displayNoInternetBanner()
        }

        setObserver()
        setView()

    }

    private fun setFacebookConsent(checked: Boolean) {
        FacebookSdk.setAutoLogAppEventsEnabled(checked)
        FacebookSdk.setAdvertiserIDCollectionEnabled(checked)
        FacebookSdk.setAutoInitEnabled(checked)
    }

    private fun setView() {
        switchConsent.setOnCheckedChangeListener { _, isChecked ->
            FirebaseAnalytics.getInstance(this)
                .setAnalyticsCollectionEnabled(isChecked)
            setFacebookConsent(isChecked)
            prefManager?.isConsentAccepted = isChecked
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                buttonTerms.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryOnDark))
                buttonTerms.style(R.style.TermsButtonSelected)
                buttonTerms.isClickable = true
                buttonTerms.isFocusable = true
                setContinueClickListener()
            } else {
                buttonTerms.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.primaryDisabled
                    )
                )
                buttonTerms.style(R.style.TermsButtonUnSelected)
                buttonTerms.isClickable = false
                buttonTerms.isFocusable = false
            }
        }

        textViewStatement.setOnClickListener {
            if (InternetUtil.isInternetOn()) {
                showErrorDialog(PRIVACY_URL)
            } else {
                displayNoInternetBanner()
            }
        }

        textViewTermsOfUse.setOnClickListener {
            if (InternetUtil.isInternetOn()) {
                showErrorDialog(TERMS_URL)
            } else {
                displayNoInternetBanner()
            }
        }


        switchConsent.setOnCheckedChangeListener { _, isChecked ->
            val prefManager = PrefManager(_context = this)
            prefManager.isConsentAccepted = isChecked
        }

        setConsentToggleText()
    }

    private fun setConsentToggleText() {
        // make part of the text clickable and set color
        val consentSpannableString =
            SpannableString(getText(R.string.share_my_usage_data_to_help_improve_the_app))
        val clickablePart = getString(R.string.cookie_notice_text)
        val clickableColor = getColor(R.color.primaryOnLight)
        consentInfo.movementMethod = LinkMovementMethod.getInstance()
        consentInfo.text = consentSpannableString.withClickableSpan(clickablePart, clickableColor) {
            if (InternetUtil.isInternetOn()) {
                showErrorDialog(COOKIES_NOTICE_URL)
            } else {
                displayNoInternetBanner()
            }
        }
    }

    private fun setObserver() {
        InternetUtil.observe(this, Observer(viewModel::onCheckNetworkConnection))
        viewModel.modelNetworkConnection.observe(
            this,
            Observer(::observeNetworkConnection)
        )
    }


    private fun observeNetworkConnection(model: TermsViewModel.NetworkModel) {
        when (model) {
            is TermsViewModel.NetworkModel.NetworkOnline -> {
                Timber.d("no internet")
            }
            is TermsViewModel.NetworkModel.NetworkOffline -> {
                displayNoInternetBanner()
            }
        }
    }

    private fun displayNoInternetBanner() {
        if (noInternetBanner.isVisible) {
            return
        }
        val totalDistance = noInternetBanner.height.toFloat()
        noInternetBanner?.slideVertically(0F)
        Handler().postDelayed({
            noInternetBanner?.slideVertically(-totalDistance, hide = true)
        }, NO_INTERNET_BANNER_DELAY)
    }


    private fun setContinueClickListener() {
        buttonTerms.setOnClickListener {
            if (InternetUtil.isInternetOn()) {
                val prefManager = PrefManager(_context = this)
                prefManager.isTermsAccepted = true
                goToCameraActivity()
            } else {
                displayNoInternetBanner()
            }
        }
    }

    private fun openBrowser(URL: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(URL))
        startActivity(browserIntent)
    }

    private fun showErrorDialog(URL: String) {
        val alertDialog: AlertDialog
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
        startActivity<CameraLightFinderActivity> {}
        overrideAnimation()
        finish()
    }

    private fun overrideAnimation() =
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
}
