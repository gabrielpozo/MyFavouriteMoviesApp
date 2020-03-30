package com.light.finder.ui.lightfinder

import android.animation.Animator
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.light.domain.model.Product
import com.light.finder.R
import com.light.finder.common.VisibilityCallBack
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.data.source.remote.ProductParcelable
import com.light.finder.di.modules.DetailComponent
import com.light.finder.di.modules.DetailModule
import com.light.finder.extensions.app
import com.light.finder.extensions.deparcelizeCategory
import com.light.finder.extensions.getViewModel
import com.light.finder.extensions.newInstance
import com.light.finder.ui.BaseFragment
import com.light.finder.extensions.*
import com.light.finder.ui.adapters.DetailImageAdapter
import com.light.finder.ui.adapters.getColorString
import com.light.finder.ui.adapters.getFinishString
import com.light.finder.ui.adapters.getColorString
import com.light.finder.ui.adapters.getFinishString
import com.light.finder.ui.lightfinder.ProductOptionsFragment.Companion.PRODUCT_LIST_EXTRA
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.DetailViewModel
import kotlinx.android.synthetic.main.custom_button_cart.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.layout_detail_bottom_sheet.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


class DetailFragment : BaseFragment() {
    companion object {
        const val PRODUCTS_ID_KEY = "ProductsFragment::id"
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var component: DetailComponent
    private lateinit var alertDialog: AlertDialog
    private lateinit var visibilityCallBack: VisibilityCallBack
    private var productSapId: String = ""
    private val viewModel: DetailViewModel by lazy { getViewModel { component.detailViewModel } }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component = app.applicationComponent.plus(DetailModule())
        } ?: throw Exception("Invalid Activity")

        setNavigationObserver()
        setDetailObservers()

        arguments?.let { bundle ->
            bundle.getParcelable<CategoryParcelable>(PRODUCTS_ID_KEY)
                ?.let { categoryParcelable ->
                    val category = categoryParcelable.deparcelizeCategory()
                    viewModel.onRetrieveProduct(category)
                    layoutChangeVariation.setOnClickListener {
                        viewModel.onChangeVariationClick()
                    }
                }
        }

        buttonAddTocart.setOnClickListener {
            viewModel.onRequestAddToCart(productSapId = productSapId)
            cartAnimation.visible()
            cartAnimation.playAnimation()
            buttonAddTocart.isClickable = false
            buttonAddTocart.isFocusable = false
        }

        cartAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                visibilityCallBack.onBottomBarBlocked(isClickable = false)
            }

            override fun onAnimationEnd(animation: Animator) {
                visibilityCallBack.onBottomBarBlocked(isClickable = true)

                cartButtonText.text = getString(R.string.added_to_cart)

                MainScope().launch {
                    delay(3000)
                    cartButtonText?.text = getString(R.string.add_to_cart)
                    cartAnimation?.gone()
                    buttonAddTocart?.isClickable = true
                    buttonAddTocart?.isFocusable = true

                }
            }

            override fun onAnimationCancel(animation: Animator) {


                cartButtonText?.text = getString(R.string.add_to_cart)
                cartAnimation?.gone()
                buttonAddTocart?.isClickable = true
                buttonAddTocart?.isFocusable = true


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


    private fun setDetailObservers() {
        viewModel.model.observe(viewLifecycleOwner, Observer(::observeProductContent))
        viewModel.modelContentVariation.observe(
            viewLifecycleOwner,
            Observer(::observeProductContentVariation)
        )
        viewModel.modelRequest.observe(viewLifecycleOwner, Observer(::observeUpdateUi))
        viewModel.modelDialog.observe(viewLifecycleOwner, Observer(::observeErrorResponse))
        viewModel.modelItemCountRequest.observe(viewLifecycleOwner, Observer(::observeItemCount))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            visibilityCallBack = context as VisibilityCallBack
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
    }


    private fun observeUpdateUi(contentCart: DetailViewModel.RequestModelContent) {

        if (contentCart.cartItem.peekContent().success.isNotEmpty()) {
            Timber.d("egeee ${contentCart.cartItem.peekContent().product.name}")
            viewModel.onRequestGetItemCount()

        } else {
            Timber.e("egeee add to cart failed! probably item is out of stock")
            cartAnimation.cancelAnimation()
            //todo error dialog
        }

    }

    private fun observeItemCount(itemCount: DetailViewModel.RequestModelItemCount) {
        val itemQuantity = itemCount.itemCount.peekContent().itemQuantity
        if (itemQuantity > 0) {
            visibilityCallBack.onBadgeCountChanged(itemQuantity)
        } else {
            Timber.d("egee Cart is empty")
        }

    }

    private fun observeErrorResponse(modelErrorEvent: Event<DetailViewModel.DialogModel>) {
        Timber.e("Add to cart failed")
        cartAnimation.cancelAnimation()
        //todo error dialog
    }

    private fun setNavigationObserver() {
        viewModel.modelNavigation.observe(viewLifecycleOwner, Observer(::navigateToProductList))
    }

    private fun observeProductContent(contentProduct: DetailViewModel.Content) {
        setViewPager(contentProduct.product)
        populateProductData(contentProduct.product)
        productSapId = contentProduct.product.sapID12NC.toString()

    }

    private fun observeProductContentVariation(contentProductVariation: DetailViewModel.ContentVariation) {
        setViewPager(contentProductVariation.product)
        populateProductData(contentProductVariation.product)
    }

    private fun navigateToProductList(navigationModel: Event<DetailViewModel.NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            mFragmentNavigation.pushFragment(
                ProductOptionsFragment.newInstance(
                    navModel.productList,
                    this
                )
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == ProductOptionsFragment.REQUEST_CODE_PRODUCT) {
                val productList: List<Product> =
                    data?.getParcelableArrayListExtra<ProductParcelable>(PRODUCT_LIST_EXTRA)
                        ?.deparcelizeProductList() ?: emptyList()
                viewModel.onRetrieveListFromProductVariation(productList)
            }
        }
    }

    private fun populateProductData(product: Product) {
        val packs = String.format(
            getString(R.string.form_factor_pack),
            product.formfactorType,
            product.qtyLampSku
        )
        val isDimmable = if (product.dimmingCode == 0) "" else "Dimmable"
        val title = String.format(
            getString(R.string.product_title),
            product.categoryName,
            isDimmable,
            product.wattageReplaced,
            product.factorBase,
            product.factorShape,
            packs
        ).capitalize()

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
            product.wattageReplaced,
            product.colorCctCode.getColorString(requireContext()),
            product.productFinishCode.getFinishString(requireContext())
        )

        textViewDetailTitle.text = title.trim().replace(Regex("(\\s)+"), " ")
        textViewDetailPricePerPack.text = pricePack
        textViewDetailPrice.text = priceLamp
        textViewDetailVariation.text = changeVariation
        textViewDetailDescription.text = product.description
    }


    private fun setViewPager(product: Product) {
        //todo add dot indicator
        //val dotsIndicator = view?.findViewById<SpringDotsIndicator>(R.id.dotsIndicator)
        val myList: MutableList<String> = mutableListOf()
        myList.addAll(product.imageUrls)
        // myList.add("https://s3.us-east-2.amazonaws.com/imagessimonprocessed/HAL_A19_E26_FROSTED.jpg")
        viewPagerDetail.adapter = DetailImageAdapter(requireContext(), myList)
        // dotsIndicator?.setViewPager(viewPagerDetail)

    }
}

