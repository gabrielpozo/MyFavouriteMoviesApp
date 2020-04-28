package com.light.finder.ui.about

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.light.finder.BuildConfig
import com.light.finder.R
import com.light.finder.ui.BaseFragment
import kotlinx.android.synthetic.main.about_fragment.*


class AboutFragment : BaseFragment() {

    companion object {
        const val TERMS_URL =
            "https://www.signify.com/global/terms-of-use/mobile-apps/signify-lightfinder-en"
        const val PRIVACY_URL =
            "https://www.signify.com/global/privacy/legal-information/privacy-notice"
    }


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
    }

    @SuppressLint("SetTextI18n")
    private fun setView() {
        textViewVersion.text = getString(R.string.version) + " " + BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE.toString()
    }

    private fun setClickListeners() {
        layoutTerms.setOnClickListener {
            //todo popup
            openBrowser(TERMS_URL)
        }

        layoutPrivacy.setOnClickListener {
            //todo popup
            openBrowser(PRIVACY_URL)
        }
    }


    private fun openBrowser(URL: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(URL))
        startActivity(browserIntent)
    }
}