package com.light.finder.ui.terms

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.airbnb.paris.extensions.style
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.CameraActivity
import com.light.finder.R
import com.light.finder.common.ConnectivityRequester
import com.light.finder.common.InternetUtil
import com.light.finder.common.PrefManager
import com.light.finder.di.modules.TermsComponent
import com.light.finder.di.modules.TermsModule
import com.light.finder.extensions.*
import com.light.finder.ui.about.AboutFragment
import com.light.presentation.viewmodels.TermsViewModel
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*
import kotlinx.android.synthetic.main.layout_reusable_dialog.view.*
import timber.log.Timber

class TermsAndConditionsActivity : AppCompatActivity() {

    companion object {
        const val TERMS_URL =
            "https://www.signify.com/global/terms-of-use/mobile-apps/signify-lightfinder-en"
        const val PRIVACY_URL =
            "https://www.signify.com/global/privacy/legal-information/privacy-notice"
        const val NO_INTERNET_BANNER_DELAY = 5000L
    }

    private var prefManager: PrefManager? = null
    private lateinit var component: TermsComponent
    private lateinit var alertDialog: AlertDialog
    private val viewModel: TermsViewModel by lazy { getViewModel { component.termsViewModel } }
    private lateinit var connectivityRequester: ConnectivityRequester

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

        component = app.applicationComponent.plus(TermsModule())
        connectivityRequester = ConnectivityRequester(this)

        prefManager = PrefManager(this)
        switchConsent.isChecked = prefManager?.isConsentAccepted!!

        setObserver()
        setView()

    }

    private fun setView() {
        switchConsent.setOnCheckedChangeListener { _, isChecked ->
            FirebaseAnalytics.getInstance(this)
                .setAnalyticsCollectionEnabled(isChecked)
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
            noInternetBanner.slideVertically(-totalDistance, hide = true)
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
        startActivity<CameraActivity> {}
        overrideAnimation()
        finish()
    }

    private fun overrideAnimation() =
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
}
