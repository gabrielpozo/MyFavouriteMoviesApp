package com.light.finder.ui.lightfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.domain.model.Product
import com.light.finder.R
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.di.modules.DetailComponent
import com.light.finder.di.modules.DetailModule
import com.light.finder.extensions.app
import com.light.finder.extensions.deparcelizeCategory
import com.light.finder.extensions.getViewModel
import com.light.finder.extensions.newInstance
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.DetailImageAdapter
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.layout_detail_bottom_sheet.*


class DetailFragment : BaseFragment() {
    companion object {
        const val PRODUCTS_ID_KEY = "ProductsFragment::id"
    }

    private lateinit var component: DetailComponent
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
                        viewModel.onChangeVariationClick(category)
                    }
                }
        }
    }


    private fun setDetailObservers() {
        viewModel.model.observe(viewLifecycleOwner, Observer(::observeProductContent))
    }

    private fun observeProductContent(contentProduct: DetailViewModel.Content) {
        setViewPager(contentProduct.product)
        populateProductData(contentProduct.product)
    }

    private fun setNavigationObserver() {
        viewModel.modelNavigation.observe(viewLifecycleOwner, Observer(::navigateToProductList))
    }

    private fun navigateToProductList(navigationModel: Event<DetailViewModel.NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            mFragmentNavigation.pushFragment(ProductOptionsFragment.newInstance(navModel.category))
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
        )

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
            product.colorCctCode,
            product.finish
        )

        textViewDetailTitle.text = title.trim().replace(Regex("(\\s)+"), " ")
        textViewDetailPricePerPack.text = pricePack
        textViewDetailPrice.text = priceLamp
        textViewDetailVariation.text = changeVariation
        textViewDetailDescription.text = product.description
    }


    private fun setViewPager(product: Product) {
        //todo set viewpager with images
        //addBottomDots(0, product.imageUrls.size)
        val myList: MutableList<String> = mutableListOf()
        myList.addAll(product.imageUrls)
        myList.add("https://s3.us-east-2.amazonaws.com/imagessimonprocessed/HAL_A19_E26_FROSTED.jpg")
        viewPagerDetail.adapter = DetailImageAdapter(requireContext(), myList)
    }

    /*private fun addBottomDots(currentPage: Int, layouts: Int) {

        val dots = arrayOfNulls<TextView>(layouts.size)

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        dotsLayout.removeAllViews()
        for (i in 0 until dots.length) {
            dots[i] = TextView(this)
            dots[i].setText(Html.fromHtml("&#8226;"))
            dots[i].setTextSize(35)
            dots[i].setTextColor(colorsInactive[currentPage])
            dotsLayout.addView(dots[i])
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage])
    }*/


}

