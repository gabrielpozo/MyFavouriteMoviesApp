package com.light.finder.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.light.finder.ui.BaseFragment
import kotlinx.android.synthetic.main.cart_fragment.*
import timber.log.Timber


class CartFragment : BaseFragment() {
    companion object {
        const val URL = "https://www.store.lightguide.signify.com/us/checkout/cart/"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.light.finder.R.layout.cart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadWebView(URL)


    }

    private fun loadWebView(url: String) {
        try {
            webView.webViewClient = WebViewClient()
            webView.settings.setSupportZoom(true)
            webView.settings.allowContentAccess = true
            webView.settings.builtInZoomControls = true
            webView.settings.displayZoomControls = false

            webView.loadUrl(url)
        } catch (e: Exception) {
            Timber.w("setUpNavigationView")
        }

    }


}