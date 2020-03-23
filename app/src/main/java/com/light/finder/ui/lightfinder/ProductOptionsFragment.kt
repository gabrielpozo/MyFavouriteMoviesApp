package com.light.finder.ui.lightfinder

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.domain.model.FilterColor
import com.light.domain.model.FilterFinish
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
                        categoryParcelable.deparcelizeCategory().categoryProducts
                    )
                }

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
        /*     Log.d(
                 "Gabriel",
                 "FilterNew observeFilteringWattage SIZE ${filteringWattage.filteredWattageButtons.size}"
             )
             filteringWattage.filteredWattageButtons.forEach {
                 Log.d(
                     "Gabriel",
                     "FilterNew: ${it.nameFilter} is selected?? ${it.isSelected} and is available?? ${it.isAvailable}"
                 )
             }
     */
        filteringWattage.filteredWattageButtons.forEach {
            Log.d(
                "GabrielDebug",
                "FilterWattage: ${it.nameFilter} -- IsSelected: ${it.isSelected} -- isAvailable: ${it.isAvailable}"
            )
        }
        if (!filteringWattage.isUpdated) {
            filterWattageAdapter.filterListWattage = filteringWattage.filteredWattageButtons
        } else {
            Log.d("GabrielUPDATE","UPDATING  HERE WATTAGE")
            filterWattageAdapter.updateLayoutBackground(filteringWattage.filteredWattageButtons)

        }

    }

    private fun observeFilteringColor(filteringColor: FilteringColor) {
        if (!filteringColor.isUpdated) {
            filterColorAdapter.filterListColor = filteringColor.filteredColorButtons
        } else {
            filterColorAdapter.updateLayoutBackground(filteringColor.filteredColorButtons)
        }
    }


    private fun observeFinishWattage(filterFinish: FilteringFinish) {
        if (!filterFinish.isUpdated) {
            filterFinishAdapter.filterListFinish = filterFinish.filteredFinishButtons
        } else {
            filterFinishAdapter.updateLayoutBackground(filterFinish.filteredFinishButtons)

        }
    }

    private fun handleFilterWattagePressed(filter: FilterWattage) {
        val mylist = mutableListOf<FilterWattage>()
        // filter.isSelected = true
        //mylist.add(filter.copy(nameFilter = "60", isSelected = true))

        //filterWattageAdapter.filterListWattage = mylist.toList()

        viewModel.onFilterWattageTap(filter)
    }

    private fun handleFilterColorPressed(filter: FilterColor) {
        // viewModel.onFilterTap(filter)
    }

    private fun handleFilterFinishPressed(filter: FilterFinish) {
        // viewModel.onFilterTap(filter)
    }


}