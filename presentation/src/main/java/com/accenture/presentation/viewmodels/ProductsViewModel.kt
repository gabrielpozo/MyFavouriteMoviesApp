package com.accenture.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.accenture.domain.model.Category
import com.accenture.domain.model.Filter
import com.accenture.domain.model.Product
import com.accenture.usecases.GetFilterButtonsUseCase
import com.accenture.usecases.GetProductsFilteredUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class ProductsViewModel(
    private val getProductListFiltered: GetProductsFilteredUseCase,
    private val getFilterButtonListUseCase: GetFilterButtonsUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    private lateinit var dataProducts: List<Product>

    private val _productsFiltered = MutableLiveData<ProductContent>()
    val productsFiltered: LiveData<ProductContent>
        get() {
            return _productsFiltered
        }


    private val _filterTags = MutableLiveData<FilteringModel>()
    val filterTags: LiveData<FilteringModel>
        get() {
            return _filterTags
        }


    data class ProductContent(val productList: List<Product>)
    data class FilteringModel(val filterList: MutableList<Filter>)


    fun onRetrieveProductsAndFilters(category: Category) {
        setDataProducts(category.categoryProducts)
        launch {
            getFilterButtonListUseCase.execute(
                ::handleFilteringButtonsResult,
                params = *arrayOf(dataProducts)
            )

            handleProductListResult(category.categoryProducts)
        }
    }


    //here we are going to get the filters associated with the products displayed on screen
    fun onFilterTap(filter: Filter) {
        launch {
            // we get the PRODUCTS associated with the filters we have
            getProductListFiltered.execute(
                ::handleProductListResult,
                params = *arrayOf(
                    dataProducts,
                    _filterTags.value?.filterList
                )
            )

            //we get the FILTERS associated with the products already filtered
            getFilterButtonListUseCase.execute(
                ::handleFilteringButtonsResult,
                params = *arrayOf(_productsFiltered.value?.productList,filter)
            )
        }
    }


    private fun handleFilteringButtonsResult(filterButtons: List<Filter>) {
        _filterTags.value = FilteringModel(filterList = filterButtons.toMutableList())
    }

    private fun handleProductListResult(products: List<Product>) {
        _productsFiltered.value = ProductContent(productList = products)
    }

    private fun handleErrorResponse(throwable: Throwable) {
        //TODO
    }

    private fun setDataProducts(productList: List<Product>) {
        dataProducts = productList
    }


}