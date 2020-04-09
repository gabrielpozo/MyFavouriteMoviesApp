package com.light.finder.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import com.light.finder.common.VisibilityCallBack
import com.light.finder.di.modules.CartComponent
import com.light.finder.di.modules.CartModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.finder.extensions.gone
import com.light.finder.extensions.visible
import com.light.finder.ui.BaseFragment
import com.light.presentation.viewmodels.CartViewModel
import kotlinx.android.synthetic.main.cart_fragment.*
import timber.log.Timber




class CartFragment : BaseFragment() {
    companion object {
        const val URL = "https://www.store.lightguide.signify.com/us/checkout/cart/"
    }

    private lateinit var component: CartComponent
    private lateinit var visibilityCallBack: VisibilityCallBack
    private val viewModel: CartViewModel by lazy { getViewModel { component.cartViewModel } }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            visibilityCallBack = context as VisibilityCallBack
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
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
        activity?.run {
            component = app.applicationComponent.plus(CartModule())
        } ?: throw Exception("Invalid Activity")
        viewModel.modelItemCountRequest.observe(viewLifecycleOwner, Observer(::observeItemCount))
        setupWebView()
    }



    private fun observeItemCount(itemCount: CartViewModel.RequestModelItemCount) {
        val itemQuantity = itemCount.itemCount.peekContent().itemQuantity
        when {
            itemQuantity > 0 ->
                visibilityCallBack.onBadgeCountChanged(itemQuantity)
            else -> Timber.d("egee Cart is empty")
        }

    }

    
    fun requestItemCount() =  viewModel.onRequestGetItemCount()

    fun reloadWebView() = webView.reload()


    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebView() {

        val webViewClient: WebViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visible()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.gone()
                super.onPageFinished(view, url)
            }
        }
        webView.webViewClient = webViewClient
        webView.settings.javaScriptEnabled = true
        webView.settings.defaultTextEncodingName = "utf-8"

        loadWebView(URL)

    }

    private fun loadWebView(url: String) {
        try {
            webView.settings.setSupportZoom(true)
            webView.settings.allowContentAccess = true
            webView.settings.builtInZoomControls = true
            webView.settings.displayZoomControls = false

            webView.loadUrl(url)
        } catch (e: Exception) {
            Timber.w("can't load website")
        }

    }


}