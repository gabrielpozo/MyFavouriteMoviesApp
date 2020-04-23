package com.light.finder.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.light.finder.CameraActivity
import com.light.finder.common.*
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
        const val NO_INTERNET_BANNER_DELAY = 5000L
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
            component = (activity as CameraActivity).lightFinderComponent.plus(CartModule())
            connectivityRequester = ConnectivityRequester(this)
        } ?: throw Exception("Invalid Activity")
        setObserver()
        setupWebView()
    }


    private fun setObserver() {
        InternetUtil.observe(viewLifecycleOwner, Observer(viewModel::onCheckNetworkConnection))
        viewModel.modelItemCountRequest.observe(viewLifecycleOwner, Observer(::observeItemCount))
        viewModel.modelReload.observe(viewLifecycleOwner, Observer(::observeProductContent))
        viewModel.modelNetworkConnection.observe(
            viewLifecycleOwner,
            Observer(::observeNetworkConnection)
        )
    }

    private fun observeProductContent(modelReload: CartViewModel.ContentReload) {
        when (modelReload) {
            CartViewModel.ContentReload.ContentToBeLoaded -> {
                webView.loadUrl(URL)
                reloadingCallback.setCurrentlyReloaded(false)
            }
            CartViewModel.ContentReload.ContentOnCheckProcess -> {
            }
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

    fun onRequestItemCount() = viewModel.onRequestGetItemCount()


    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webChromeClient: WebChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                if (newProgress < 100 && progressBar.isGone) {
                    progressBar.showWithAnimation()
                }

                if (newProgress == 100) {
                    progressBar.hideWithAnimation()
                }

                progressBar.progress = newProgress
            }
        }

        val webViewClient: WebViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                view?.scrollTo(0, 0)
                viewModel.onSetWebUrl(url.getSplitUrl())
                super.onPageFinished(view, url)
            }
        }
        webView.webViewClient = webViewClient
        webView.webChromeClient = webChromeClient
        webView.settings.javaScriptEnabled = true
        webView.settings.defaultTextEncodingName = "utf-8"


        try {
            webView.settings.setSupportZoom(true)
            webView.settings.allowContentAccess = true
            webView.settings.builtInZoomControls = true
            webView.settings.displayZoomControls = false

        } catch (e: Exception) {
            Timber.w("can't load website")
        }


    }


    private fun observeNetworkConnection(model: CartViewModel.NetworkModel) {
        when (model) {
            is CartViewModel.NetworkModel.NetworkOnline -> {
                //   webView.reload()
                webView.visible()
            }
            is CartViewModel.NetworkModel.NetworkOffline -> {
                webView.invisible()
                displayNoInternetBanner()
            }
        }
    }

    private fun displayNoInternetBanner() {
        if (no_internet_banner_cart.isVisible) {
            return
        }
        val totalDistance = no_internet_banner_cart.height.toFloat() + cart_toolbar.height.toFloat()
        no_internet_banner_cart?.slideVertically(0F)
        Handler().postDelayed({
            no_internet_banner_cart.slideVertically(-totalDistance, hide = true)
        }, NO_INTERNET_BANNER_DELAY)
    }

    fun onLoadWebView() {
        viewModel.onCheckReloadCartWebView(reloadingCallback.hasBeenReload())
    }


    fun onCheckIfOffline() {
        if (!InternetUtil.isInternetOn()) displayNoInternetBanner()
    }
}