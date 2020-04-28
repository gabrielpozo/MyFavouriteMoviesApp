package com.light.finder.ui.about

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.light.finder.BuildConfig
import com.light.finder.R
import com.light.finder.ui.BaseFragment
import kotlinx.android.synthetic.main.about_fragment.*

class AboutFragment : BaseFragment() {
    companion object;
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
        textViewVersion.text = getString(R.string.version) + " " + BuildConfig.VERSION_NAME
    }

    private fun setClickListeners() {
        layoutTerms.setOnClickListener {
            goToTermsActivity()
        }

        layoutPrivacy.setOnClickListener {
            goToPrivacyStatementActivity()
        }


    }

    private fun goToTermsActivity() = screenNavigator.navigateToTermsScreen()
    private fun goToPrivacyStatementActivity() = screenNavigator.navigateToPrivacyScreen()
}