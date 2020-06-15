package com.light.finder.ui.lightfinder

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
import com.light.finder.common.ReloadingCallback
import com.light.finder.data.source.local.LocalPreferenceDataSourceImpl
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.di.modules.submodules.DetailComponent
import com.light.finder.di.modules.submodules.DetailModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.DetailImageAdapter
import com.light.finder.ui.adapters.FilterColorAdapter
import com.light.finder.ui.adapters.FilterFinishAdapter
import com.light.finder.ui.adapters.FilterWattageAdapter
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.DetailViewModel
import com.light.source.local.LocalPreferenceDataSource
import kotlinx.android.synthetic.main.custom_button_cart.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.layout_detail_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_filter_dialog.*
import kotlinx.android.synthetic.main.layout_reusable_dialog.view.*
import kotlinx.android.synthetic.main.product_title_banner.*
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
    private var isSingleProduct: Boolean = false
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

        firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.view_product)) {
            putString(getString(R.string.parameter_sku), productSapId)
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
    }

    private fun setBottomSheetBehaviour() {
        val bottomSheetLayout = view?.findViewById<NestedScrollView>(R.id.bottomSheetLayout)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        // avoid unwanted scroll when bottom sheet collapsed
        ViewCompat.setNestedScrollingEnabled(recyclerViewWattage, false);
        ViewCompat.setNestedScrollingEnabled(recyclerViewColor, false);
        ViewCompat.setNestedScrollingEnabled(recyclerViewFinish, false);

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
                    if (!isSingleProduct) {
                        // for future use?
                    }

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
                if (!isSingleProduct) {
                    // for future use?
                }

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
        viewModel.model.observe(viewLifecycleOwner, Observer(::observeProductContent))
        viewModel.modelRequest.observe(viewLifecycleOwner, Observer(::observeUpdateUi))
        viewModel.modelDialog.observe(viewLifecycleOwner, Observer(::observeErrorResponse))
        viewModel.modelItemCountRequest.observe(viewLifecycleOwner, Observer(::observeItemCount))
    }


    private fun observeUpdateUi(contentCart: DetailViewModel.RequestModelContent) {
        if (contentCart.cartItem.peekContent().success.isNotEmpty()) {
            val product = contentCart.cartItem.peekContent().product
            Timber.d("egeee ${product.name}")
            firebaseAnalytics.logEventOnGoogleTagManager("add_to_cart") {
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

    private fun observeErrorResponse(modelErrorEvent: Event<DetailViewModel.ServerError>) {
        Timber.e("Add to cart failed")
        cartAnimation.cancelAnimation()
        showErrorDialog(
            getString(R.string.sorry),
            getString(R.string.cannot_added),
            getString(R.string.ok),
            false
        )
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
            alertDialog.dismiss()
        }

        dialogView.buttonNegative.gone()
        dialogView.buttonNeutral.gone()
        alertDialog.show()

    }

    private fun setNavigationObserver() {
        viewModel.modelNavigation.observe(viewLifecycleOwner, Observer(::navigateToProductList))
    }


    private fun observeProductContent(contentProduct: DetailViewModel.Content) {
        isSingleProduct = contentProduct.isSingleProduct
        setViewPager(contentProduct.product)
        populateProductData(contentProduct.product)
        populateStickyHeaderData(contentProduct.product)
        productSapId = contentProduct.product.sapID12NC.toString()
        pricePerPack = contentProduct.product.pricePack
    }

    private fun navigateToProductList(navigationModel: Event<DetailViewModel.NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            screenNavigator.navigateToVariationScreen(navModel.productList)
        }
    }

    private fun populateProductData(product: Product) {
        val title = product.name

        val pricePack = String.format(
            getString(R.string.price_per_pack),
            product.pricePack
        )

        val priceLamp = String.format(
            getString(R.string.price_detail),
            product.priceLamp
        )


        /*       val changeVariation = String.format(
                   getString(R.string.change_variation),
                   getLegendTagPref(
                       product.colorCctCode,
                       logError = true,
                       isForDetailScreen = true,
                       filterTypeList = localPreferences.loadLegendCctFilterNames(),
                       legendTag = "product_cct_code"
                   ),
                   product.wattageReplaced,
                   getLegendTagPref(
                       product.productFinishCode, true,
                       isForDetailScreen = true,
                       filterTypeList = localPreferences.loadLegendFinishFilterNames(),
                       legendTag = "product_finish_code"
                   ),
                   getString(R.string.finish)
               )*/

        textViewDetailTitle.text = title
        textViewDetailPricePerPack.text = pricePack
        textViewDetailPrice.text = priceLamp


        val drawableStart = requireContext().getColorDrawable(product.colorCctCode)
        if (drawableStart == 0) {
            //imageViewColor.visibility = View.GONE
        } else {
//            imageViewColor.visibility = View.VISIBLE
//            imageViewColor.setImageDrawable(requireContext().getDrawable(drawableStart))
        }
        textViewDetailDescription.text = product.description

        if (isSingleProduct) {
//            val param = imageViewColor.layoutParams as ViewGroup.MarginLayoutParams
//            param.marginStart = 4
//            imageViewColor.layoutParams = param
//            linearVariationContainer.setBackgroundResource(R.drawable.not_outlined)
//            linearVariationContainer.isClickable = false
//            textViewDetailChange.visibility = View.GONE
//            imageViewArrow.visibility = View.INVISIBLE
        }
    }

    private fun populateStickyHeaderData(product: Product) {
        // product sticky header
        val formFactorType = getLegendTagPref(
            product.formfactorType,
            filterTypeList = localPreferences.loadLegendFormFactorFilterNames(),
            legendTag = FORM_FACTOR_LEGEND_TAG
        )

        val bannerPacks = String.format(
            getString(R.string.banner_packs),
            product.qtySkuCase,
            product.qtySkuCase.pluralOrSingular(),
            product.qtyLampSku,
            formFactorType,
            product.qtyLampSku.pluralOrSingular()
        )

        val bannerTitle = String.format(
            getString(R.string.banner_title),
            product.categoryName, product.wattageReplaced, product.factorBase
        )

        val pricePerPack = String.format(
            getString(R.string.banner_price),
            product.pricePack
        )

        product_banner_title.text = bannerTitle
        product_banner_packs.text = bannerPacks
        banner_price.text = pricePerPack
    }

    private fun setViewPager(product: Product) {
        val productImageList: MutableList<String> = mutableListOf()
        //productImageList.add("https://s3.us-east-2.amazonaws.com/imagessimonprocessed/HAL_A19_E26_FROSTED.jpg")
        productImageList.addAll(product.imageUrls)

        if (productImageList.size == 0) {
            productImageList.add("")
        } else if (productImageList.size > 1) {
            isSingleImage = false
        }

        viewPagerDetail.adapter = DetailImageAdapter(requireContext(), productImageList)
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
                    productTitleBanner?.slideVertically(0F, 250)
                } else {
                    productTitleBanner?.slideVertically(-productTitleBanner.height.toFloat(), 200)
                }
            }
        })
        setImageGalleryDots()
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

    /***
     *
     *
     */

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
            Observer(::observeFinishWattage)
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
        filterColorAdapter.filterListColor = filteringColor.filteredColorButtons

    }

    private fun observeFinishWattage(filterFinish: DetailViewModel.FilteringFinish) {
        if (filterFinish.isUpdated) {
            filterFinishAdapter.updateBackgroundAppearance(filterFinish.filteredFinishButtons)
        }
        filterFinishAdapter.filterListFinish = filterFinish.filteredFinishButtons
    }

    private fun observeProductSelectedResult(productSelectedModel: DetailViewModel.ProductSelectedModel) {
        productSapId = productSelectedModel.productSelected.sapID12NC.toString()
        pricePerPack = productSelectedModel.productSelected.pricePack

        populateProductData(productSelectedModel.productSelected)
        populateStickyHeaderData(productSelectedModel.productSelected)

        textViewWattage.text = String.format(
            getString(R.string.wattage_variation),
            productSelectedModel.productSelected.wattageReplaced.toString()
        )
        textViewColor.text = getLegendTagPref(
            productSelectedModel.productSelected.colorCctCode,
            filterTypeList = localPreferences.loadLegendCctFilterNames(),
            legendTag = COLOR_LEGEND_TAG
        )
        textViewFinish.text = getLegendTagPref(
            productSelectedModel.productSelected.productFinishCode,
            filterTypeList = localPreferences.loadLegendFinishFilterNames(),
            legendTag = FINISH_LEGEND_TAG
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

}

