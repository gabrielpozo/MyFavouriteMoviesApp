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
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.light.domain.model.Category
import com.light.domain.model.Product
import com.light.finder.R
import com.light.finder.common.ConnectivityRequester
import com.light.finder.common.ReloadingCallback
import com.light.finder.common.VisibilityCallBack
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.di.modules.DetailComponent
import com.light.finder.di.modules.DetailModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.DetailImageAdapter
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.DetailViewModel
import kotlinx.android.synthetic.main.custom_button_cart.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.layout_detail_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_reusable_dialog.view.*
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
    private lateinit var visibilityCallBack: VisibilityCallBack
    private lateinit var reloadingCallback: ReloadingCallback
    private lateinit var connectivityRequester: ConnectivityRequester
    private var isSingleProduct: Boolean = false

    private var productSapId: String = ""
    private var pricePerPack: Float = 0.0F


    private val viewModel: DetailViewModel by lazy { getViewModel { component.detailViewModel } }

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
    ): View? = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component = lightFinderComponent.plus(DetailModule())
            connectivityRequester = ConnectivityRequester(this)

        } ?: throw Exception("Invalid Activity")

        setNavigationObserver()
        setDetailObservers()


        arguments?.let { bundle ->
            bundle.getParcelable<CategoryParcelable>(PRODUCTS_ID_KEY)
                ?.let { categoryParcelable ->
                    val category = categoryParcelable.deparcelizeCategory()
                    viewModel.onRetrieveProduct(category)
                    checkCodesValidity(category)
                    linearVariationContainer.setOnClickListener {
                        viewModel.onChangeVariationClick()
                    }
                }
        }

        buttonAddTocart.setOnClickListener {
            connectivityRequester.checkConnection { isConnected ->
                if (isConnected) {
                    addToCart()
                } else {
                    visibilityCallBack.onInternetConnectionLost()
                }
            }
        }

        cartAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                visibilityCallBack.onBottomBarBlocked(isClickable = false)
                linearVariationContainer.isClickable = false
                linearVariationContainer.isFocusable = false
                cartButtonText.text = getString(R.string.adding_to_cart)
            }

            override fun onAnimationEnd(animation: Animator) {
                visibilityCallBack.onBottomBarBlocked(isClickable = true)
                if (!isSingleProduct) {
                    linearVariationContainer.isClickable = true
                    linearVariationContainer.isFocusable = true
                }

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
                visibilityCallBack.onBottomBarBlocked(isClickable = true)
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


        val bottomSheetLayout = view.findViewById<NestedScrollView>(R.id.bottomSheetLayout)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        context?.let {
            val displayMetrics = it.resources.displayMetrics
            val dpHeight = displayMetrics.heightPixels
            viewPagerDetail.updateLayoutParams<ViewGroup.LayoutParams> {
                height = (dpHeight / 2)
            }
            bottomSheetBehavior.peekHeight = (dpHeight / 2)
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
                    R.color.primaryPressed
                )
            }?.let { it2 ->
                buttonAddTocart.setBackgroundColor(
                    it2
                )
            }

        }
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
                putFloat("VALUE",pricePerPack)
            }
            viewModel.onRequestGetItemCount()

        } else {
            Timber.e("egeee add to cart failed! probably item is out of stock")
            cartAnimation.cancelAnimation()
            showErrorDialog(
                getString(R.string.unable_to_add),
                getString(R.string.connection_issue),
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
                    visibilityCallBack.onBadgeCountChanged(itemQuantity)
                }, 3000)

            }
            else -> Timber.d("egee Cart is empty")
        }

    }

    private fun observeErrorResponse(modelErrorEvent: Event<DetailViewModel.DialogModel>) {
        Timber.e("Add to cart failed")
        cartAnimation.cancelAnimation()
        showErrorDialog(
            getString(R.string.unable_to_add),
            getString(R.string.connection_issue),
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
        productSapId = contentProduct.product.sapID12NC.toString()
        pricePerPack = contentProduct.product.pricePack
    }

    private fun navigateToProductList(navigationModel: Event<DetailViewModel.NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            screenNavigator.navigateToVariationScreen(navModel.productList)
        }
    }


    fun retrieveLisFromProductVariation(productList: List<Product>) {
        viewModel.onRetrieveListFromProductVariation(productList)
    }

    private fun populateProductData(product: Product) {
        val packs = String.format(
            getString(R.string.form_factor_pack),
            requireContext().getformFactortType(product.formfactorType),
            product.qtyLampSku
        )
        //val isDimmable = if (product.dimmingCode == 0) "" else "Dimmable"
        var title = String.format(
            getString(R.string.product_title),
            product.categoryName,
            product.wattageReplaced,
            product.factorBase,
            packs
        )

        val space = " "
        val splitedStr = title.split(space)
        title = splitedStr.joinToString(space) {
            if (it != "packs" || it != "pack") {
                it.capitalize()
            }
            it
        }

        val pricePack = String.format(
            getString(R.string.price_per_pack),
            product.pricePack
        )

        val priceLamp = String.format(
            getString(R.string.price_detail),
            product.priceLamp
        )

        val changeVariation = String.format(
            getString(R.string.change_variation),
            requireContext().getColorName(product.colorCctCode, true),
            product.wattageReplaced,
            requireContext().getFinishName(product.productFinishCode, true)
        )

        textViewDetailTitle.text = title.trim().replace(Regex("(\\s)+"), " ")
        textViewDetailPricePerPack.text = pricePack
        textViewDetailPrice.text = priceLamp
        textViewDetailVariation.text = changeVariation
        val drawableStart = requireContext().getColorDrawable(product.colorCctCode)
        if (drawableStart == 0) {
            imageViewColor.visibility = View.GONE
        } else {
            imageViewColor.setImageDrawable(requireContext().getDrawable(drawableStart))
        }
        textViewDetailDescription.text = product.description

        if (isSingleProduct) {
            linearVariationContainer.setBackgroundResource(R.drawable.not_outlined)
            linearVariationContainer.isClickable = false
            textViewDetailChange.visibility = View.GONE
            imageViewArrow.visibility = View.INVISIBLE
        }
    }

    private fun setViewPager(product: Product) {
        val productImageList: MutableList<String> = mutableListOf()
        //productImageList.add("https://s3.us-east-2.amazonaws.com/imagessimonprocessed/HAL_A19_E26_FROSTED.jpg")
        productImageList.addAll(product.imageUrls)

        when (productImageList.size) {
            0 -> {
                productImageList.add("")
            }
        }

        viewPagerDetail.adapter = DetailImageAdapter(requireContext(), productImageList)
        setImageGalleryDots(productImageList)
    }

    private fun setImageGalleryDots(productImageList: MutableList<String>) {
        if (productImageList.size < 2) {
            return
        }

        dots_indicator?.visibility = View.VISIBLE
        dots_indicator?.attachViewPager(viewPagerDetail)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, newState: Int) {
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

            }
        })
    }

    private fun checkCodesValidity(category: Category) {
        checkCategoryColorCodesAreValid(category.colors)
        checkCategoryFinishCodesAreValid(category.finishCodes)
    }
}

