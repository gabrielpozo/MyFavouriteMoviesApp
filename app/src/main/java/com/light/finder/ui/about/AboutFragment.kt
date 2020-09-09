package com.light.finder.ui.about

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.facebook.FacebookSdk
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.BuildConfig
import com.light.finder.CameraLightFinderActivity
import com.light.finder.R
import com.light.finder.common.ConnectivityRequester
import com.light.finder.common.InternetUtil
import com.light.finder.common.PrefManager
import com.light.finder.di.modules.submodules.AboutComponent
import com.light.finder.di.modules.submodules.AboutModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.presentation.viewmodels.AboutViewModel
import kotlinx.android.synthetic.main.about_fragment.*
import kotlinx.android.synthetic.main.about_fragment.consentInfo
import kotlinx.android.synthetic.main.about_fragment.layoutPrivacy
import kotlinx.android.synthetic.main.about_fragment.noInternetBanner
import kotlinx.android.synthetic.main.about_fragment.switchConsent
import kotlinx.android.synthetic.main.layout_reusable_dialog.view.*
import timber.log.Timber


class AboutFragment : BaseFragment() {

    companion object {
        const val TERMS_URL =
            "https://www.signify.com/global/terms-of-use/mobile-apps/signify-lightfinder-en"
        const val PRIVACY_URL =
            "https://www.signify.com/global/privacy/legal-information/privacy-notice"
        const val FAQ_URL = "https://www.signify.com/en-us/lightfinder/support"
        const val COOKIES_NOTICE_URL =
            "https://www.signify.com/global/privacy/legal-information/cookies-notice"
        const val NO_INTERNET_BANNER_DELAY = 5000L
    }

    private lateinit var component: AboutComponent
    private lateinit var alertDialog: AlertDialog
    private var prefManager: PrefManager? = null
    private val viewModel: AboutViewModel by lazy { getViewModel { component.aboutViewModel } }
    private lateinit var connectivityRequester: ConnectivityRequester


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component =
                (activity as CameraLightFinderActivity).lightFinderComponent.plus(AboutModule())
            connectivityRequester = ConnectivityRequester(this)
        } ?: throw Exception("Invalid Activity")
        setClickListeners()
        setView()
        setObserver()

        prefManager = PrefManager(requireContext())
        switchConsent.isChecked = prefManager?.isConsentAccepted!!
        FirebaseAnalytics.getInstance(requireContext())
            .setAnalyticsCollectionEnabled(switchConsent.isChecked)

        setFacebookConsent(switchConsent.isChecked)
    }

    private fun setFacebookConsent(checked: Boolean) {
        FacebookSdk.setAutoLogAppEventsEnabled(checked)
        FacebookSdk.setAdvertiserIDCollectionEnabled(checked)
        FacebookSdk.setAutoInitEnabled(checked)
        facebookAnalyticsUtil.setConsent(checked)
    }

    private fun setObserver() {
        InternetUtil.observe(viewLifecycleOwner, Observer(viewModel::onCheckNetworkConnection))
        viewModel.modelNetworkConnection.observe(
            viewLifecycleOwner,
            Observer(::observeNetworkConnection)
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setView() {
        textViewVersion.text =
            getString(R.string.version) + " " + BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE.toString()

        switchConsent.setOnCheckedChangeListener { _, isChecked ->
            val prefManager = PrefManager(_context = requireContext())
            FirebaseAnalytics.getInstance(requireContext())
                .setAnalyticsCollectionEnabled(isChecked)
            setFacebookConsent(isChecked)
            prefManager.isConsentAccepted = isChecked
        }

        setConsentToggleText()

    }

    private fun setClickListeners() {
        layoutTerms.setSafeOnClickListener {
            if (InternetUtil.isInternetOn()) {
                showAboutDialog(AboutDialogFlags.TERMS)
            } else {
                displayNoInternetBanner()
            }
        }

        layoutPrivacy.setSafeOnClickListener {
            if (InternetUtil.isInternetOn()) {
                showAboutDialog(AboutDialogFlags.PRIVACY)
            } else {
                displayNoInternetBanner()
            }
        }



        layoutFaq.setSafeOnClickListener {
            if (InternetUtil.isInternetOn()) {
                showAboutDialog(AboutDialogFlags.FAQ)
            } else {
                displayNoInternetBanner()
            }
        }

        layoutFeedback.setSafeOnClickListener {
            screenNavigator.navigateToUsabillaForm()

        }

    }

    private fun setConsentToggleText() {
        context?.let {
            // make part of the text clickable and set color
            val consentSpannableString =
                SpannableString(getText(R.string.share_my_usage_data_to_help_improve_the_app))
            val clickablePart = getString(R.string.cookie_notice_text)
            val clickableColor = getColor(it, R.color.primaryOnLight)
            consentInfo.movementMethod = LinkMovementMethod.getInstance()
            consentInfo.text =
                consentSpannableString.withClickableSpan(clickablePart, clickableColor) {
                    if (InternetUtil.isInternetOn()) {
                        showAboutDialog(AboutDialogFlags.COOKIES)
                    } else {
                        displayNoInternetBanner()
                    }
                }
        }
    }

    private fun observeNetworkConnection(model: AboutViewModel.NetworkModel) {
        when (model) {
            is AboutViewModel.NetworkModel.NetworkOnline -> {
                Timber.d("network online")
            }
            is AboutViewModel.NetworkModel.NetworkOffline -> {
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
        //TODO investigate why this is triggered from camera fragment
        Handler().postDelayed({
            noInternetBanner?.slideVertically(-totalDistance, hide = true)
        }, NO_INTERNET_BANNER_DELAY)
    }


    private fun openBrowser(URL: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(URL))
        startActivity(browserIntent)
    }

    private fun showAboutDialog(aboutFlag: AboutDialogFlags) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
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
            when (aboutFlag) {
                AboutDialogFlags.PRIVACY -> {
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.open_privacy_notice)) {}
                }
                AboutDialogFlags.TERMS -> {
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.open_terms_of_use)) {}
                }
                AboutDialogFlags.FAQ -> {
                    //firebaseAnalytics.logEventOnGoogleTagManager("") {}
                }
            }
            alertDialog.dismiss()
            openBrowser(aboutFlag.url)
        }
        dialogView.buttonNeutral.visible()
        dialogView.buttonNeutral.setOnClickListener {
            alertDialog.dismiss()
        }
        dialogView.buttonNegative.gone()
        alertDialog.show()

    }

    fun setLightStatusBar() {
        var flags = view?.systemUiVisibility
        if (flags != null) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view?.systemUiVisibility = flags
        }
    }

    enum class AboutDialogFlags(val url: String) {
        PRIVACY(PRIVACY_URL), TERMS(TERMS_URL), FAQ(FAQ_URL), COOKIES(
            COOKIES_NOTICE_URL
        )
    }
}