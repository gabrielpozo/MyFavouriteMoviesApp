package com.light.finder.ui.lightfinder

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.domain.model.FilterVariationCF
import com.light.finder.R
import com.light.finder.common.VisibilityCallBack
import com.light.finder.data.source.remote.ProductParcelable
import com.light.finder.di.modules.ProductsOptionsComponent
import com.light.finder.di.modules.ProductsOptionsModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.*
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.ProductsOptionsViewModel
import com.light.presentation.viewmodels.ProductsOptionsViewModel.*
import kotlinx.android.synthetic.main.layout_filter_dialog.*
import java.lang.ClassCastException


class ProductOptionsFragment : BaseFragment() {

    companion object {
        const val PRODUCTS_OPTIONS_ID_KEY = "ProductsOptionsFragment::id"
        const val PRODUCT_LIST_EXTRA = "productListId"
        const val REQUEST_CODE_PRODUCT = 1
    }

    private lateinit var component: ProductsOptionsComponent
    private val viewModel: ProductsOptionsViewModel by lazy { getViewModel { component.productsOptionsViewModel } }
    private lateinit var visibilityCallBack: VisibilityCallBack
    private lateinit var filterWattageAdapter: FilterWattageAdapter
    private lateinit var filterColorAdapter: FilterColorAdapter
    private lateinit var filterFinishAdapter: FilterFinishAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_filter_dialog, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            visibilityCallBack = context as VisibilityCallBack
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            component = app.applicationComponent.plus(ProductsOptionsModule())
        } ?: throw Exception("Invalid Activity")

        initAdapters()

        arguments?.let { bundle ->

            bundle.getParcelableArrayList<ProductParcelable>(PRODUCTS_OPTIONS_ID_KEY)
                ?.let { productList ->
                    viewModel.onRetrieveProductsVariation(
                        productList.deparcelizeProductList()
                    )
                }

            setVariationsObservers()
            navigationObserver()
            setDoneClickListener()
        }
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

    private fun navigationObserver() {
        viewModel.modelNavigation.observe(
            viewLifecycleOwner,
            Observer(::navigateBackToDetail)
        )
    }

    private fun setDoneClickListener() {
        textViewDone.setOnClickListener {
            viewModel.onDoneButtonClicked()
        }
    }

    private fun initAdapters() {
        filterWattageAdapter = FilterWattageAdapter(::handleFilterWattagePressed)
        recyclerViewWattage.adapter = filterWattageAdapter

        filterColorAdapter = FilterColorAdapter(::handleFilterColorPressed)
        recyclerViewColor.adapter = filterColorAdapter

        filterFinishAdapter = FilterFinishAdapter(::handleFilterFinishPressed)
        recyclerViewFinish.adapter = filterFinishAdapter
    }

    private fun observeFilteringWattage(filteringWattage: FilteringWattage) {
        if (filteringWattage.isUpdated) {
            filterWattageAdapter.updateBackgroundAppearance(filteringWattage.filteredWattageButtons)
        }
        filterWattageAdapter.filterListWattage = filteringWattage.filteredWattageButtons
    }

    private fun observeFilteringColor(filteringColor: FilteringColor) {
        if (filteringColor.isUpdated) {
            filterColorAdapter.updateBackgroundAppearance(filteringColor.filteredColorButtons)
        }
        filterColorAdapter.filterListColor = filteringColor.filteredColorButtons

    }

    private fun observeFinishWattage(filterFinish: FilteringFinish) {
        if (filterFinish.isUpdated) {
            filterFinishAdapter.updateBackgroundAppearance(filterFinish.filteredFinishButtons)
        }
        filterFinishAdapter.filterListFinish = filterFinish.filteredFinishButtons
    }

    private fun observeProductSelectedResult(productSelectedModel: ProductSelectedModel) {
        //String.format(getString(R.string.wattage_variation),productSelectedModel.productSelected.wattageReplaced.toString() )
        textViewWattage.text = String.format(
            getString(R.string.wattage_variation),
            productSelectedModel.productSelected.wattageReplaced.toString()
        )
        textViewColor.text =productSelectedModel.productSelected.colorCctCode.getColorString(requireContext())
        textViewFinish.text = productSelectedModel.productSelected.finish

    }

    private fun navigateBackToDetail(navigationModel: Event<NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            initializeIntent<ProductOptionsFragment> {
                putParcelableArrayListExtra(
                    PRODUCT_LIST_EXTRA,
                    navModel.categoryProducts.parcelizeProductList()
                )
                targetFragment?.onActivityResult(targetRequestCode, RESULT_OK, this)
                mFragmentNavigation.popFragment()
            }
        }
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