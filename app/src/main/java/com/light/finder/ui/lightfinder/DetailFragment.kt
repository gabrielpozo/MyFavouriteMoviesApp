package com.light.finder.ui.lightfinder

import android.Manifest
import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.light.domain.model.Category
import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.finder.R
import com.light.finder.common.ActivityCallback
import com.light.finder.common.ConnectivityRequester
import com.light.finder.common.PermissionRequester
import com.light.finder.common.ReloadingCallback
import com.light.finder.data.source.local.LocalPreferenceDataSourceImpl
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.di.modules.submodules.DetailComponent
import com.light.finder.di.modules.submodules.DetailModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.*
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.DetailViewModel
import com.light.source.local.LocalPreferenceDataSource
import kotlinx.android.synthetic.main.custom_button_cart.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.layout_detail_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_filter_dialog.*
import kotlinx.android.synthetic.main.layout_reusable_dialog.view.*
import kotlinx.android.synthetic.main.layout_sticky_header.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class DetailFragment : BaseFragment() {
    companion object {
        const val PRODUCTS_ID_KEY = "ProductsFragment::id"
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var component: DetailComponent
    private lateinit var alertDialog: AlertDialog
    private lateinit var activityCallback: ActivityCallback
    private lateinit var reloadingCallback: ReloadingCallback
    private lateinit var connectivityRequester: ConnectivityRequester
    private lateinit var filterWattageAdapter: FilterWattageAdapter
    private lateinit var filterColorAdapter: FilterColorAdapter
    private lateinit var filterFinishAdapter: FilterFinishAdapter
    private lateinit var filterConnectivityAdapter: FilterConnectivityAdapter
    private lateinit var cameraPermissionRequester: PermissionRequester


    private var isSingleImage: Boolean = true
    private val localPreferences: LocalPreferenceDataSource by lazy {
        LocalPreferenceDataSourceImpl(
            requireContext()
        )
    }

    private var productSapId: String = ""
    private var pricePerPack: Float = 0.0F


    private val viewModel: DetailViewModel by lazy { getViewModel { component.detailViewModel } }

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
    ): View? = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component = lightFinderComponent.plus(DetailModule())
            connectivityRequester = ConnectivityRequester(this)
            cameraPermissionRequester = PermissionRequester(this, Manifest.permission.CAMERA)

        } ?: throw Exception("Invalid Activity")



        setNavigationObserver()
        setDetailObservers()
        setLightStatusBar()

        arguments?.let { bundle ->
            bundle.getParcelable<CategoryParcelable>(PRODUCTS_ID_KEY)
                ?.let { categoryParcelable ->
                    val category = categoryParcelable.deparcelizeCategory()
                    //viewModel.onRetrieveProduct(category)
                    viewModel.onRetrieveProductsVariation(category.categoryProducts)
                    checkCodesValidity(category)
                }
        }

        buttonAddTocart.setOnClickListener {
            connectivityRequester.checkConnection { isConnected ->
                if (isConnected) {
                    addToCart()
                } else {
                    activityCallback.onInternetConnectionLost()
                }
            }
        }


        initAdapters()
        setVariationsObservers()
        setCartListeners()
        setBottomSheetBehaviour()
        setViewPager()

    }

    private fun setBottomSheetBehaviour() {
        val bottomSheetLayout = view?.findViewById<NestedScrollView>(R.id.bottomSheetLayout)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        // avoid unwanted scroll when bottom sheet collapsed
        ViewCompat.setNestedScrollingEnabled(recyclerViewWattage, false)
        ViewCompat.setNestedScrollingEnabled(recyclerViewColor, false)
        ViewCompat.setNestedScrollingEnabled(recyclerViewFinish, false)

        context?.let {
            val displayMetrics = it.resources.displayMetrics
            val dpHeight = displayMetrics.heightPixels
            viewPagerDetail.updateLayoutParams<ViewGroup.LayoutParams> {
                height = (dpHeight / 2)
            }
            bottomSheetBehavior.peekHeight = (dpHeight / 2.5).toInt()
        }
    }


    private fun addToCart() {
        viewModel.onRequestAddToCart(productSapId = productSapId)
        cartAnimation.visible()
        cartAnimation.playAnimation()
        buttonAddTocart.isClickable = false
        buttonAddTocart.isFocusable = false
        if (isAdded) {
            reloadingCallback.setCurrentlyReloaded(true)
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.primaryOnDark
                )
            }?.let { it2 ->
                buttonAddTocart.setBackgroundColor(
                    it2
                )
            }
        }
    }


    private fun setCartListeners() {
        cartAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                activityCallback.onBottomBarBlocked(isClickable = false)
                cartButtonText.text = getString(R.string.adding_to_cart)
            }

            override fun onAnimationEnd(animation: Animator) {
                activityCallback.onBottomBarBlocked(isClickable = true)


                cartButtonText.text = getString(R.string.added_to_cart)

                MainScope().launch {
                    delay(3000)
                    cartButtonText?.text = getString(R.string.add_to_cart)
                    cartAnimation?.invisible()
                    buttonAddTocart?.isClickable = true
                    buttonAddTocart?.isFocusable = true

                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.primaryOnDark
                        )
                    }?.let { it2 ->
                        buttonAddTocart.setBackgroundColor(
                            it2
                        )
                    }
                }
            }

            override fun onAnimationCancel(animation: Animator) {
                activityCallback.onBottomBarBlocked(isClickable = true)
                cartButtonText?.text = getString(R.string.add_to_cart)
                cartAnimation?.invisible()
                buttonAddTocart?.isClickable = true
                buttonAddTocart?.isFocusable = true

                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.primaryOnDark
                    )
                }?.let { it2 ->
                    buttonAddTocart.setBackgroundColor(
                        it2
                    )
                }
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })


    }

    private fun setDetailObservers() {
        viewModel.modelSapId.observe(viewLifecycleOwner, Observer(::observeProductSapId))
        viewModel.modelRequest.observe(viewLifecycleOwner, Observer(::observeUpdateUi))
        viewModel.modelDialog.observe(viewLifecycleOwner, Observer(::observeDialogStatus))
        viewModel.modelItemCountRequest.observe(viewLifecycleOwner, Observer(::observeItemCount))
        viewModel.modelCctType.observe(viewLifecycleOwner, Observer(::observeCctType))
        viewModel.modelPermissionStatus.observe(
            viewLifecycleOwner,
            Observer(::observePermissionStatus)
        )
    }

    private fun observeProductSapId(contentCart: DetailViewModel.ContentProductId) {
        productSapId = contentCart.productSapId
        logger.logEventOnFacebookSdk(getString(R.string.view_product)) {
            putString(getString(R.string.parameter_sku), productSapId)
        }
        firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.view_product)) {
            putString(getString(R.string.parameter_sku), productSapId)
        }
    }

    private fun observeUpdateUi(contentCart: DetailViewModel.RequestModelContent) {
        if (contentCart.cartItem.peekContent().success.isNotEmpty()) {
            val product = contentCart.cartItem.peekContent().product
            Timber.d("egeee ${product.name}")
            logger.logEventOnFacebookSdk(getString(R.string.add_to_cart_fb)) {
                putString(getString(R.string.parameter_sku), productSapId)
                putDouble(getString(R.string.value), pricePerPack.toDouble())
            }
            firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.add_to_cart)) {
                putString("CURRENCY", "USD")
                putString("ITEMS", productSapId)
                putFloat("VALUE", pricePerPack)
            }
            viewModel.onRequestGetItemCount()

        } else {
            Timber.e("egeee add to cart failed! probably item is out of stock")
            cartAnimation.cancelAnimation()
            showErrorDialog(
                getString(R.string.sorry),
                getString(R.string.cannot_added),
                getString(R.string.ok),
                false
            )
        }

    }

    private fun observeItemCount(itemCount: DetailViewModel.RequestModelItemCount) {
        val itemQuantity = itemCount.itemCount.peekContent().itemQuantity
        when {
            itemQuantity > 0 -> {
                val handler = Handler()
                handler.postDelayed({
                    activityCallback.onBadgeCountChanged(itemQuantity)
                }, 3000)

            }
            else -> Timber.d("egee Cart is empty")
        }

    }

    private fun observeDialogStatus(modelErrorEvent: Event<DetailViewModel.DialogModel>) {
        modelErrorEvent.getContentIfNotHandled()?.let { modelDialog ->
            when (modelDialog) {
                is DetailViewModel.DialogModel.ServerError -> {
                    Timber.e("Add to cart failed")
                    cartAnimation.cancelAnimation()
                    showErrorDialog(
                        getString(R.string.sorry),
                        getString(R.string.cannot_added),
                        getString(R.string.ok),
                        false
                    )

                }
                is DetailViewModel.DialogModel.PermissionPermanentlyDenied -> {
                    showErrorDialog(
                        getString(R.string.enable_live_preview_access),
                        getString(R.string.enable_live_preview_subtitle),
                        getString(R.string.enable_camera_button),
                        true
                    )
                }
                is DetailViewModel.DialogModel.ProductNotFound -> {
                    cartAnimation.cancelAnimation()
                    showErrorDialog(
                        getString(R.string.oops),
                        getString(R.string.product_not_found_description),
                        getString(R.string.ok),
                        false
                    )
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.add_to_cart_error)) {
                        putString(getString(R.string.parameter_sku), productSapId)
                        putString(getString(R.string.error_reason_event), getString(R.string.product_not_found_event_tag))

                    }
                }
                is DetailViewModel.DialogModel.OutStock -> {
                    cartAnimation.cancelAnimation()
                    showErrorDialog(
                        getString(R.string.out_of_stock),
                        getString(R.string.out_of_stock_description),
                        getString(R.string.ok),
                        false
                    )
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.add_to_cart_error)) {
                        putString(getString(R.string.parameter_sku), productSapId)
                        putString(getString(R.string.error_reason_event), getString(R.string.out_of_stock_event_tag))

                    }
                }
                is DetailViewModel.DialogModel.ProductDisable -> {
                    cartAnimation.cancelAnimation()
                    showErrorDialog(
                        getString(R.string.oops),
                        getString(R.string.product_disable_description),
                        getString(R.string.ok),
                        false
                    )
                    firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.add_to_cart_error)) {
                        putString(getString(R.string.parameter_sku), productSapId)
                        putString(getString(R.string.error_reason_event), getString(R.string.product_disabled_event_tag))
                    }
                }

            }
        }

    }


    private fun observeCctType(modelCctTypeEvent: Event<DetailViewModel.CctColorsSelected>) {
        modelCctTypeEvent.getContentIfNotHandled()?.let { contentCctList ->
            screenNavigator.navigateToLiveAmbiance(contentCctList.cctTypeList)
        }
    }

    private fun observePermissionStatus(modelPermissionStatus: DetailViewModel.PermissionStatus) {
        when (modelPermissionStatus) {
            is DetailViewModel.PermissionStatus.PermissionGranted -> {
                viewModel.onRetrievingCctSelectedColors(localPreferences.loadLegendCctFilterNames())
            }
            is DetailViewModel.PermissionStatus.PermissionDenied -> {
                //TODO Permission Denied
            }
        }
    }

    private fun showErrorDialog(
        titleDialog: String,
        subtitleDialog: String,
        buttonPositiveText: String,
        neutralButton: Boolean = true
    ) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.layout_reusable_dialog, null)
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.window?.setDimAmount(0.6f)
        dialogView.textViewTitleDialog.text = titleDialog
        dialogView.textViewSubTitleDialog.text = subtitleDialog
        dialogView.buttonPositive.text = buttonPositiveText

        dialogView.buttonPositive.setOnClickListener {
            if (neutralButton) {
                viewModel.onGoToSettingsClicked()
            } else {
                alertDialog.dismiss()
            }
        }


        dialogView.buttonNegative.gone()
        if (neutralButton) {
            dialogView.buttonNeutral.text = getString(R.string.not_now)
            dialogView.buttonNeutral.setOnClickListener {
                alertDialog.dismiss()
            }

        } else {
            dialogView.buttonNeutral.gone()
        }
        alertDialog.show()

    }

    private fun setNavigationObserver() {
        viewModel.modelNavigation.observe(viewLifecycleOwner, Observer(::navigateToSettings))
    }


    private fun navigateToSettings(navigationModel: Event<DetailViewModel.NavigationModelSettings>) {
        navigationModel.getContentIfNotHandled()?.let {
            screenNavigator.navigateToSettings()
            alertDialog.dismiss()
        }
    }

    private fun populateProductData(product: Product) {
        setImageAdapter(product)
        val title = product.name

        val pricePack = String.format(
            getString(R.string.price_per_pack),
            product.pricePack
        )

        val priceLamp = String.format(
            getString(R.string.price_detail),
            product.priceLamp
        )
        textViewDetailTitle.text = title
        textViewDetailPricePerPack.text = pricePack
        textViewDetailPrice.text = priceLamp

        textViewDetailDescription.text = product.description

    }

    private fun populateStickyHeaderData(product: Product) {
        // product sticky header

        val pricePerPack = String.format(
            getString(R.string.sticky_header_price),
            product.pricePack
        )

        sticky_header_title.text = product.stickyHeaderFirstLine
        sticky_header_packs.text = product.stickyHeaderSecondLine
        sticky_header_price.text = pricePerPack

        sticky_header_packs.invalidate()
        sticky_header_packs.requestLayout()
    }

    private fun setLivePreviewButton(product: Product) {
        if (getLegendArTypeTagPref(
                product.colorCctCode,
                localPreferences.loadLegendCctFilterNames()
            )
        ) {
            livePreviewButton.visible()
            livePreviewButtonDisabled.gone()
        } else {
            livePreviewButton.gone()
            livePreviewButtonDisabled.visible()
        }


        livePreviewButton.setSafeOnClickListener {
            if (getLegendArTypeTagPref(
                    product.colorCctCode,
                    localPreferences.loadLegendCctFilterNames()
                )
            ) {
                cameraPermissionRequester.request({ isPermissionGranted ->
                    viewModel.onCameraPermissionRequested(isPermissionGranted)
                }, (::observePermanentlyDeniedPermission))
            }
        }
    }

    private fun observePermanentlyDeniedPermission(isPermanentlyDenied: Boolean) {
        viewModel.onPermissionDenied(isPermanentlyDenied, false)

        //TODO
    }

    private fun setViewPager() {
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, newState: Int) {
                if (isSingleImage) {
                    return
                }
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        dots_indicator.visibility = View.VISIBLE
                    }
                    else -> {
                        dots_indicator.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(p0: View, p1: Float) {
                p0.height
                p1.toString()
                if (p1 == 1F) {
                    stickyHeaderTitle?.slideVertically(0F, 100)
                } else {
                    stickyHeaderTitle?.slideVertically(-stickyHeaderTitle.height.toFloat(), 75)
                }
            }
        })
        setImageGalleryDots()
    }

    private fun setImageAdapter(product: Product) {
        val productImageList: MutableList<String> = mutableListOf()
        //productImageList.add("https://s3.us-east-2.amazonaws.com/imagessimonprocessed/HAL_A19_E26_FROSTED.jpg")
        productImageList.addAll(product.imageUrls)

        if (productImageList.size == 0) {
            productImageList.add("")
        } else if (productImageList.size > 1) {
            isSingleImage = false
        }
        viewPagerDetail.adapter = DetailImageAdapter(requireContext(), productImageList)
    }

    private fun setImageGalleryDots() {

        if (isSingleImage) {
            return
        }

        dots_indicator?.visibility = View.VISIBLE
        dots_indicator?.attachViewPager(viewPagerDetail)
    }

    private fun checkCodesValidity(category: Category) {
        checkCategoryColorCodesAreValid(category.colors)
        checkCategoryFinishCodesAreValid(category.finishCodes)
    }

    private fun setLightStatusBar() {
        var flags = view?.systemUiVisibility
        if (flags != null) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view?.systemUiVisibility = flags
        }
    }


    private fun initAdapters() {
        filterWattageAdapter = FilterWattageAdapter(::handleFilterWattagePressed)
        recyclerViewWattage.adapter = filterWattageAdapter

        filterColorAdapter = FilterColorAdapter(
            ::handleFilterColorPressed,
            localPreferences.loadLegendCctFilterNames()
        )
        recyclerViewColor.adapter = filterColorAdapter

        filterFinishAdapter = FilterFinishAdapter(
            ::handleFilterFinishPressed,
            localPreferences.loadLegendFinishFilterNames()
        )
        recyclerViewFinish.adapter = filterFinishAdapter

        filterConnectivityAdapter = FilterConnectivityAdapter(
            ::handleFilterConnectivityPressed,
            localPreferences.loadLegendConnectivityNames()
        )
        recyclerViewConnectivity.adapter = filterConnectivityAdapter

    }


    private fun setVariationsObservers() {
        viewModel.dataFilterWattageButtons.observe(
            viewLifecycleOwner,
            Observer(::observeFilteringWattage)
        )

        viewModel.dataFilterColorButtons.observe(
            viewLifecycleOwner,
            Observer(::observeFilteringColor)
        )

        viewModel.dataFilterFinishButtons.observe(
            viewLifecycleOwner,
            Observer(::observeFilteringFinish)
        )

        viewModel.dataFilterConnectivityButtons.observe(
            viewLifecycleOwner,
            Observer(::observeFilteringConnectivity)
        )

        viewModel.productSelected.observe(
            viewLifecycleOwner,
            Observer(::observeProductSelectedResult)
        )
    }

    private fun observeFilteringWattage(filteringWattage: DetailViewModel.FilteringWattage) {
        if (filteringWattage.isUpdated) {
            filterWattageAdapter.updateBackgroundAppearance(filteringWattage.filteredWattageButtons)
        }
        filterWattageAdapter.filterListWattage = filteringWattage.filteredWattageButtons
    }

    private fun observeFilteringColor(filteringColor: DetailViewModel.FilteringColor) {
        if (filteringColor.isUpdated) {
            filterColorAdapter.updateBackgroundAppearance(filteringColor.filteredColorButtons)
        }
        filterColorAdapter.filterListColor =
            filteringColor.filteredColorButtons.sortColorByOrderField(localPreferences.loadLegendCctFilterNames())
    }

    private fun observeFilteringFinish(filterFinish: DetailViewModel.FilteringFinish) {
        if (filterFinish.isUpdated) {
            filterFinishAdapter.updateBackgroundAppearance(filterFinish.filteredFinishButtons)
        }

        filterFinishAdapter.filterListFinish =
            filterFinish.filteredFinishButtons.sortFinishByOrderField(localPreferences.loadLegendFinishFilterNames())
    }

    private fun observeFilteringConnectivity(filterConnectivity: DetailViewModel.FilteringConnectivity) {
        if (filterConnectivity.isUpdated) {
            filterConnectivityAdapter.updateBackgroundAppearance(filterConnectivity.filterConnectivityButtons)
        }

        filterConnectivityAdapter.filterListConnectivity =
            filterConnectivity.filterConnectivityButtons.sortConnectivityByOrderField(
                localPreferences.loadLegendConnectivityNames()
            )
    }

    private fun observeProductSelectedResult(productSelectedModel: DetailViewModel.ProductSelectedModel) {
        //TODO(improve this logic) working along with the viewModel
        productSapId = productSelectedModel.productSelected.sapID12NC.toString()
        pricePerPack = productSelectedModel.productSelected.pricePack
        setLivePreviewButton(productSelectedModel.productSelected)
        populateProductData(productSelectedModel.productSelected)
        populateStickyHeaderData(productSelectedModel.productSelected)

        textViewWattage.text = getString(
            R.string.wattage_detail,
            productSelectedModel.productSelected.wattageReplaced.toString(),
            productSelectedModel.productSelected.wattageReplacedExtra
        )

        textViewColor.text = getLegendCctTagPref(
            productSelectedModel.productSelected.colorCctCode,
            filterTypeList = localPreferences.loadLegendCctFilterNames(),
            legendTag = COLOR_LEGEND_TAG
        )
        textViewFinish.text = getLegendFinishTagPref(
            productSelectedModel.productSelected.productFinishCode,
            filterTypeList = localPreferences.loadLegendFinishFilterNames(),
            legendTag = FINISH_LEGEND_TAG
        )
        textViewConnectivity.text = getLegendConnectivityTagPref(
            productSelectedModel.productSelected.productConnectionCode,
            filterTypeList = localPreferences.loadLegendConnectivityNames(),
            legendTag = CONNECTIVITY_LEGEND_TAG
        )

    }

    private fun handleFilterWattagePressed(filter: FilterVariationCF) {
        viewModel.onFilterWattageTap(filter)
    }

    private fun handleFilterColorPressed(filter: FilterVariationCF) {
        viewModel.onFilterColorTap(filter)
    }

    private fun handleFilterFinishPressed(filter: FilterVariationCF) {
        viewModel.onFilterFinishTap(filter)
    }

    private fun handleFilterConnectivityPressed(filter: FilterVariationCF) {
        viewModel.onFilterConnectivityTap(filter)

    }

    fun returningFromLiveAmbiance(colorCode: Int) {
        filterColorAdapter.setColorFromAmbiance(colorCode)
    }

}

