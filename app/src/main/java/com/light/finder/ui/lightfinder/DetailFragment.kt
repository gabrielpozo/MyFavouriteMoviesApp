package com.light.finder.ui.lightfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.light.domain.model.Product
import com.light.finder.R
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.di.modules.DetailComponent
import com.light.finder.di.modules.DetailModule
import com.light.finder.extensions.app
import com.light.finder.extensions.deparcelizeCategory
import com.light.finder.extensions.getViewModel
import com.light.finder.ui.adapters.DetailImageAdapter
import com.light.presentation.viewmodels.DetailViewModel
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.layout_detail_bottom_sheet.*


class DetailFragment : Fragment() {
    companion object {
        const val PRODUCTS_ID_KEY = "ProductsFragment::id"
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var component: DetailComponent
    private lateinit var alertDialog: AlertDialog
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
        arguments?.let { bundle ->
            bundle.getParcelable<CategoryParcelable>(PRODUCTS_ID_KEY)
                ?.let { categoryParcelable ->
                    viewModel.onRetrieveProduct(categoryParcelable.deparcelizeCategory())
                }
            setObservers()
        }

        layoutChangeVariation.setOnClickListener {
            openFilterDialog()
        }

        val bottomSheetLayout = view.findViewById<NestedScrollView>(R.id.bottomSheetLayout)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)


    }

    private fun openFilterDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.layout_filter_dialog, null)
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.window?.setDimAmount(0.6f)
        alertDialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
        }
        alertDialog.show()
    }

    private fun setObservers() {
        viewModel.model.observe(viewLifecycleOwner, Observer(::observeProductContent))
    }


    private fun observeProductContent(contentProduct: DetailViewModel.Content) {
        setViewPager(contentProduct.product)
        populateProductData(contentProduct.product)
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
        val dotsIndicator = view?.findViewById<SpringDotsIndicator>(R.id.dotsIndicator)
        val myList: MutableList<String> = mutableListOf()
        myList.addAll(product.imageUrls)
        myList.add("https://s3.us-east-2.amazonaws.com/imagessimonprocessed/HAL_A19_E26_FROSTED.jpg")
        viewPagerDetail.adapter = DetailImageAdapter(requireContext(), myList)
        dotsIndicator?.setViewPager(viewPagerDetail)

    }
}

