package com.light.finder.ui.lightfinder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.finder.R
import com.light.finder.common.VisibilityCallBack
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.di.modules.ProductsOptionsComponent
import com.light.finder.di.modules.ProductsOptionsModule
import com.light.finder.extensions.app
import com.light.finder.extensions.deparcelizeCategory
import com.light.finder.extensions.getViewModel
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.CategoriesAdapter
import com.light.presentation.viewmodels.ProductsOptionsViewModel
import com.light.presentation.viewmodels.ProductsOptionsViewModel.*
import com.light.presentation.viewmodels.ProductsOptionsViewModel.FilteringVariation.*
import com.light.presentation.viewmodels.ProductsViewModel
import java.lang.ClassCastException


class ProductOptionsFragment : BaseFragment() {

    companion object {
        const val PRODUCTS_OPTIONS_ID_KEY = "ProductsOptionsFragment::id"
    }

    private lateinit var component: ProductsOptionsComponent
    private val viewModel: ProductsOptionsViewModel by lazy { getViewModel { component.productsOptionsViewModel } }
    private lateinit var adapter: CategoriesAdapter
    private lateinit var visibilityCallBack: VisibilityCallBack

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
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
        /*filterAdapter = FilterAdapter(::handleFilterPressed)
        rvFilterResult.adapter = filterAdapter*/
    }

    private fun observeModelContent(filterContent: FilteringVariation) {
        when (filterContent) {
            is FilteringWattage -> {
            }
            is FilteringColor -> {
            }
            is FilteringFinish -> {
            }
        }

    }


    private fun updateWattageFilters(modelFilter: ProductsViewModel.FilteringModel) {
        //filterAdapter.filterList = modelFilter.filteredButtons
    }

    private fun updateColorsFilters(modelFilter: ProductsViewModel.FilteringModel) {
      //  filterAdapter.filterList = modelFilter.filteredButtons
    }

    private fun updateFilters(modelFilter: ProductsViewModel.FilteringModel) {
       // filterAdapter.filterList = modelFilter.filteredButtons
    }






}