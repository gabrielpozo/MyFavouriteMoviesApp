package com.light.finder.ui.lightfinder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.domain.model.FilterWattage
import com.light.finder.R
import com.light.finder.common.VisibilityCallBack
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.di.modules.ProductsOptionsComponent
import com.light.finder.di.modules.ProductsOptionsModule
import com.light.finder.extensions.app
import com.light.finder.extensions.deparcelizeCategory
import com.light.finder.extensions.getViewModel
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.*
import com.light.presentation.viewmodels.ProductsOptionsViewModel
import com.light.presentation.viewmodels.ProductsOptionsViewModel.*
import com.light.presentation.viewmodels.ProductsOptionsViewModel.FilteringVariation.*
import kotlinx.android.synthetic.main.layout_filter_dialog.*
import java.lang.ClassCastException


class ProductOptionsFragment : BaseFragment() {

    companion object {
        const val PRODUCTS_OPTIONS_ID_KEY = "ProductsOptionsFragment::id"
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
            bundle.getParcelable<CategoryParcelable>(PRODUCTS_OPTIONS_ID_KEY)
                ?.let { categoryParcelable ->
                    viewModel.onRetrieveProductsVariation(
                        categoryParcelable.deparcelizeCategory()
                    )
                }

            viewModel.dataFilterWattageButtons.observe(
                viewLifecycleOwner,
                Observer(::observeModelContent)
            )

        }

        //navigationObserver()
    }

    private fun initAdapters() {
        filterWattageAdapter = FilterWattageAdapter(::handleFilterWattagePressed)
        recyclerViewWattage.adapter = filterWattageAdapter

        filterColorAdapter = FilterColorAdapter(::handleFilterColorPressed)
        recyclerViewWattage.adapter = filterColorAdapter

        filterFinishAdapter = FilterFinishAdapter(::handleFilterWattagePressed)
        recyclerViewWattage.adapter = filterFinishAdapter
    }

    private fun observeModelContent(modelContent: FilteringVariation) {
        when (modelContent) {
            is FilteringWattage -> { updateWattageFilters(modelContent)
            }
            is FilteringColor -> {
            }
            is FilteringFinish -> {
            }
        }

    }


    private fun updateWattageFilters(filteringWattage: FilteringWattage) {
        filterWattageAdapter.filterList = filteringWattage.filteredWattageButtons
    }

    private fun updateColorsFilters(filteringColor: FilteringColor) {
          filterColorAdapter.filterList = filteringColor.filteredColorButtons
    }

    private fun updateFilters(filteringFinish : FilteringFinish) {
         filterFinishAdapter.filterList = filteringFinish.filteredFinishButtons
    }

    private fun handleFilterWattagePressed(filter: FilterWattage) {
       // viewModel.onFilterTap(filter)
    }

    private fun handleFilterColorPressed(filter: FilterWattage) {
        // viewModel.onFilterTap(filter)
    }

    private fun handleFilterFinishPressed(filter: FilterWattage) {
        // viewModel.onFilterTap(filter)
    }


}