package com.light.finder.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import com.light.finder.common.ConnectivityRequester
import com.light.finder.common.ReloadingCallback
import com.light.finder.common.VisibilityCallBack
import com.light.finder.di.modules.CartComponent
import com.light.finder.di.modules.CartModule
import com.light.finder.extensions.*
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
    private lateinit var reloadingCallback: ReloadingCallback
    private val viewModel: CartViewModel by lazy { getViewModel { component.cartViewModel } }
    private lateinit var connectivityRequester: ConnectivityRequester


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            visibilityCallBack = context as VisibilityCallBack
            reloadingCallback = context as ReloadingCallback

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
            connectivityRequester = ConnectivityRequester(this)
        } ?: throw Exception("Invalid Activity")
        setObserver()
    }


    private fun setObserver() {
        setupWebView()
        viewModel.modelItemCountRequest.observe(viewLifecycleOwner, Observer(::observeItemCount))
        viewModel.modelReload.observe(viewLifecycleOwner, Observer(::observeProductContent))
        viewModel.connectionModel.observe(viewLifecycleOwner, Observer { observeNetworkConnection() })
    }

    private fun observeProductContent(modelReload: CartViewModel.ContentReload) {
        if (modelReload.shouldReload) {
            webView.reload()
            reloadingCallback.setCurrentlyReloaded(false)
        }
    }


    private fun observeItemCount(countModel: CartViewModel.CountItemsModel) {
        when (countModel) {
            is CartViewModel.CountItemsModel.RequestModelItemCount -> {
                visibilityCallBack.onBadgeCountChanged(countModel.itemCount.peekContent().itemQuantity)
            }
            is CartViewModel.CountItemsModel.ClearedBadgeItemCount -> {
                visibilityCallBack.onCartCleared()
            }
        }
    }


    fun requestItemCount() = viewModel.onRequestGetItemCount()

    fun onReloadWebView() {
/*        connectivityRequester.checkConnection { isConnected ->
            if (!isConnected) {
                webView.invisible()
                viewModel.onInternetConnectionLost()
            } else {
                webView.reload()
                webView.visible()
            }
        }*/
        viewModel.onCheckReloadCartWebView(reloadingCallback.hasBeenReload())
    }


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
                viewModel.onSetWebUrl(url.getSplitUrl())
                viewModel.onRequestGetItemCount()
                progressBar.gone()
                super.onPageFinished(view, url)
            }
        }
        webView.webViewClient = webViewClient
        webView.settings.javaScriptEnabled = true
        webView.settings.defaultTextEncodingName = "utf-8"

        loadWebView(URL)
    }

    private fun observeNetworkConnection() {
        val totalDistance = no_internet_banner_cart.height.toFloat() + cart_toolbar.height.toFloat()
        no_internet_banner_cart?.slideVertically(0F)
        Handler().postDelayed({
            no_internet_banner_cart.slideVertically(-totalDistance, hide = true)
        }, 5000)
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