package com.light.finder.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import com.light.finder.BuildConfig
import com.light.finder.R
import com.light.finder.common.ActivityCallback
import com.light.finder.common.ConnectivityRequester
import com.light.finder.common.InternetUtil
import com.light.finder.common.ReloadingCallback
import com.light.finder.di.modules.submodules.CartComponent
import com.light.finder.di.modules.submodules.CartModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.presentation.viewmodels.CartViewModel
import com.light.util.QA
import kotlinx.android.synthetic.main.cart_fragment.*
import kotlinx.android.synthetic.main.cart_fragment_offline.*
import kotlinx.android.synthetic.main.layout_error.*
import timber.log.Timber

class CartFragment : BaseFragment() {
    companion object {
        const val URL = "https://www.store.lightguide.signify.com/us/checkout/cart/"
        const val URL_QA = "https://lightfinder-staging.luzernsolutions.com/us/checkout/cart/"
        const val NO_INTERNET_BANNER_DELAY = 5000L
    }

    private lateinit var component: CartComponent
    private lateinit var activityCallback: ActivityCallback
    private lateinit var reloadingCallback: ReloadingCallback
    private val viewModel: CartViewModel by lazy { getViewModel { component.cartViewModel } }
    private lateinit var connectivityRequester: ConnectivityRequester

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallback = context as ActivityCallback
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
        return inflater.inflate(R.layout.cart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component = lightFinderComponent.plus(CartModule())
            connectivityRequester = ConnectivityRequester(this)
        } ?: throw Exception("Invalid Activity")
        setObserver()
        setupWebView()
        observeLayout()
        setListeners()
    }

    private fun setListeners() {
        retry_internet.setSafeOnClickListener {
            if (!InternetUtil.isInternetOn()) {
                displayNoInternetBanner()
            } else {
                no_internet_overlay.gone()
            }
        }

        buttonTryAgain.setOnClickListener {
            viewModel.onRequestGetItemCount()
            viewModel.onCheckReloadCartWebView(reloadingCallback.hasBeenReload())
            hideErrorLayout()
        }
    }

    private fun observeLayout() {
        cart_fragment_root.viewTreeObserver.addOnGlobalLayoutListener {
            val rec = Rect()
            cart_fragment_root.getWindowVisibleDisplayFrame(rec)
            val screenHeight = cart_fragment_root.rootView.height

            val keypadHeight = screenHeight - rec.bottom
            val param: ViewGroup.MarginLayoutParams

            // add margin bottom when keyboard is visible
            if (keypadHeight > screenHeight * 0.15) {
                param = webView.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0, 0, 0, keypadHeight / 2)
            } else {
                param = webView.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0, 0, 0, 0)
            }

            webView.layoutParams = param
        }
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
                if (BuildConfig.FLAVOR == QA) {
                    webView.loadUrl(URL_QA)
                } else {
                    webView.loadUrl(URL)
                }
                reloadingCallback.setCurrentlyReloaded(false)
            }
            CartViewModel.ContentReload.ContentOnCheckProcess -> {
            }
        }
    }

    private fun observeItemCount(countModel: CartViewModel.CountItemsModel) {
        when (countModel) {
            is CartViewModel.CountItemsModel.RequestModelItemCount -> {
                hideErrorLayout()
                activityCallback.onBadgeCountChanged(countModel.itemCount.peekContent().itemQuantity)
            }
            is CartViewModel.CountItemsModel.ClearedBadgeItemCount -> {
                hideErrorLayout()
                activityCallback.onCartCleared()
            }
            is CartViewModel.CountItemsModel.PaymentSuccessful -> {
                hideErrorLayout()
                firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.payment_successful)) {}
                facebookAnalyticsUtil.logEventOnFacebookSdk(getString(R.string.payment_successful_fb)) {}
                activityCallback.onCartCleared()
            }
            is CartViewModel.CountItemsModel.ErrorRequestItemCount -> {
                showErrorLayout()
            }
        }
    }

    private fun showErrorLayout() {
        progressBar.gone()
        cartErrorLayout.visible()
        webView.gone()
    }

    private fun hideErrorLayout() {
        progressBar.visible()
        cartErrorLayout.gone()
        webView.visible()
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webChromeClient: WebChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar?.let {
                    setProgress(newProgress)
                }
                white_overlay.visible()
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
                if (!InternetUtil.isInternetOn()) {
                    view?.invisible()
                    displayNoInternetBanner()
                    displayNoConnectionOverlay()
                } else {
                    view?.visible()
                }

                white_overlay.gone()
                viewModel.onRequestGetItemCount()
                view?.scrollTo(0, 0)
                viewModel.onSetWebUrl(url.getSplitUrl())
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Timber.d("Ege: preventing showing webpage not available")
                view?.loadUrl("about:blank")
                super.onReceivedError(view, request, error)
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
                webView.reload()
                no_internet_overlay.gone()
            }
            is CartViewModel.NetworkModel.NetworkOffline -> {
                no_internet_overlay.visible()
            }
        }
    }

    private fun displayNoInternetBanner() {
        if (no_internet_banner_cart.translationY == 0F) {
            return
        }
        val totalDistance = no_internet_banner_cart.height.toFloat() + cart_toolbar.height.toFloat()
        no_internet_banner_cart?.slideVertically(0F)
        Handler().postDelayed({
            no_internet_banner_cart?.slideVertically(-totalDistance)
        }, NO_INTERNET_BANNER_DELAY)
    }

    fun onLoadWebView() {
        viewModel.onCheckReloadCartWebView(reloadingCallback.hasBeenReload())
    }

    private fun displayNoConnectionOverlay() {
        no_internet_overlay.visible()
    }

    private fun setProgress(newProgress: Int) {
        if (newProgress < 100 && progressBar.isGone) {
            progressBar?.showWithAnimation()
        }

        if (newProgress == 100) {
            progressBar?.hideWithAnimation()
        }

        progressBar?.progress = newProgress
    }


    override fun onStart() {
        super.onStart()
        //we do extra call to check if something has been added in browsing flow or scanning flow
        viewModel.onRequestGetItemCount()
    }

    fun onCheckIfOffline() {
        if (!InternetUtil.isInternetOn()) {
            webView.invisible()
            displayNoInternetBanner()
            displayNoConnectionOverlay()
        }
    }
}