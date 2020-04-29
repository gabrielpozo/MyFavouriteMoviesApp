package com.light.finder.ui.about

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.BuildConfig
import com.light.finder.R
import com.light.finder.common.PrefManager
import com.light.finder.extensions.gone
import com.light.finder.extensions.visible
import com.light.finder.ui.BaseFragment
import kotlinx.android.synthetic.main.about_fragment.*
import kotlinx.android.synthetic.main.about_fragment.layoutPrivacy
import kotlinx.android.synthetic.main.about_fragment.switchConsent
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*
import kotlinx.android.synthetic.main.layout_reusable_dialog.view.*


class AboutFragment : BaseFragment() {

    companion object {
        const val TERMS_URL =
            "https://www.signify.com/global/terms-of-use/mobile-apps/signify-lightfinder-en"
        const val PRIVACY_URL =
            "https://www.signify.com/global/privacy/legal-information/privacy-notice"
    }

    private lateinit var alertDialog: AlertDialog
    private var prefManager: PrefManager? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setView()

        prefManager = PrefManager(requireContext())
        switchConsent.isChecked = prefManager?.isConsentAccepted!!
        FirebaseAnalytics.getInstance(requireContext()).setAnalyticsCollectionEnabled(switchConsent.isChecked)
    }

    @SuppressLint("SetTextI18n")
    private fun setView() {
        textViewVersion.text = getString(R.string.version) + " " + BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE.toString()

        switchConsent.setOnCheckedChangeListener { _, isChecked ->
            val prefManager = PrefManager(_context = requireContext())
            prefManager.isConsentAccepted = isChecked
        }
    }

    private fun setClickListeners() {
        layoutTerms.setOnClickListener {
            showErrorDialog(TERMS_URL)
        }

        layoutPrivacy.setOnClickListener {
           showErrorDialog(PRIVACY_URL)
        }
    }


    private fun openBrowser(URL: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(URL))
        startActivity(browserIntent)
    }

    private fun showErrorDialog(URL: String) {
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
        dialogView.buttonNegative.text = getString(R.string.text_cancel)
        dialogView.buttonPositive.setOnClickListener {
            alertDialog.dismiss()
            openBrowser(URL)
        }
        dialogView.buttonNegative.visible()
        dialogView.buttonNegative.setOnClickListener {
            alertDialog.dismiss()
        }
        dialogView.buttonNeutral.gone()
        alertDialog.show()

    }
}