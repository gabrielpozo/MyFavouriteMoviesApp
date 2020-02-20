package com.light.finder.ui.lightfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.light.domain.model.Filter
import androidx.navigation.fragment.findNavController
import com.light.presentation.viewmodels.ProductsViewModel
import com.light.finder.R
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.di.modules.ProductsComponent
import com.light.finder.di.modules.ProductsModule
import com.light.finder.extensions.app
import com.light.finder.extensions.deparcelize
import com.light.finder.extensions.getViewModel
import com.light.finder.ui.adapters.FilterAdapter
import com.light.finder.ui.adapters.ProductsAdapter
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_products.rvProducts


class ProductsFragment : Fragment() {

    companion object {
        const val PRODUCTS_ID_KEY = "ProductsFragment::id"
    }

    private lateinit var component: ProductsComponent
    private val viewModel: ProductsViewModel by lazy { getViewModel { component.productsViewModel } }
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var filterAdapter: FilterAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_filter, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component = app.applicationComponent.plus(ProductsModule())
        } ?: throw Exception("Invalid Activity")

        initProductsAdapter()
        initFilterAdapter()

        arguments?.let { bundle ->
            bundle.getParcelable<CategoryParcelable>(PRODUCTS_ID_KEY)
                ?.let { categoryParcelable ->
                    viewModel.onRetrieveProductsAndFilters(categoryParcelable.deparcelize())
                }

            observeElements()
        }

    }

    private fun initFilterAdapter() {
        rvFilterResult.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
        filterAdapter = FilterAdapter(::handleFilterPressed)
        rvFilterResult.adapter = filterAdapter
    }

    private fun initProductsAdapter() {
        productsAdapter = ProductsAdapter()
        rvProducts.adapter = productsAdapter
    }


    private fun observeElements() {
        viewModel.productsFiltered.observe(viewLifecycleOwner, Observer(::updateProducts))
        viewModel.dataFilterButtons.observe(viewLifecycleOwner, Observer(::updateFilters))
    }


    private fun handleFilterPressed(filter: Filter) {
        viewModel.onFilterTap(filter)
    }

    private fun updateProducts(modelProduct: ProductsViewModel.ProductContent) {
        productsAdapter.products = modelProduct.productList
    }

    private fun updateFilters(modelFilter: ProductsViewModel.FilteringModel) {
        filterAdapter.filterList = modelFilter.filteredButtons
    }
}