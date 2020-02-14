package com.accenture.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.accenture.domain.model.Category
import com.accenture.domain.model.Filter
import com.accenture.domain.model.Product
import com.accenture.usecases.GetFilterButtonsUseCase
import com.accenture.usecases.GetProductsFilteredUseCase
import com.accenture.usecases.GetActiveFiltersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class ProductsViewModel(
    private val getProductListFiltered: GetProductsFilteredUseCase,
    private val getFilterButtonListUseCase: GetFilterButtonsUseCase,
    private val getActiveFilterButtonsUseCase: GetActiveFiltersUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    private lateinit var dataProducts: List<Product>
    private lateinit var filterActiveButtonsNoActive: List<Filter>

    private val _productsFiltered = MutableLiveData<ProductContent>()
    val productsFiltered: LiveData<ProductContent>
        get() {
            return _productsFiltered
        }


    private val _dataFilterButtons = MutableLiveData<FilteringModel>()
    val dataFilterButtons: LiveData<FilteringModel>
        get() {
            return _dataFilterButtons
        }


    data class ProductContent(val productList: List<Product>)
    data class FilteringModel(
        val initFilterList: List<Filter>?,
        val filteredButtons: List<Filter> = emptyList()
    )


    fun onRetrieveProductsAndFilters(category: Category) {
        setDataProducts(category.categoryProducts)
        launch {
            getFilterButtonListUseCase.execute(
                ::handleInitFilteringButtonsResult,
                params = *arrayOf(dataProducts)
            )

            handleProductListResult(category.categoryProducts)
        }
    }


    //here we are going to get the filters associated with the products displayed on screen
    fun onFilterTap(filter: Filter) {
        launch {
            // we get the PRODUCTS associated with the filters we have
            switchActiveFieldOnFilterInitList(filter)
            getProductListFiltered.execute(
                ::handleProductListResult,
                params = *arrayOf(dataProducts, _dataFilterButtons.value?.initFilterList)
            )

            //we get the FILTERS associated with the products already filtered
            getFilterButtonListUseCase.execute(
                ::setFilteredButtonsNoActive,
                params = *arrayOf(
                    _productsFiltered.value?.productList
                )
            )

            //We set to active those active-filters in the new filtered list and
            // we get them back to display them finally on the view
            getActiveFilterButtonsUseCase.execute(
                ::handleFilteredButtons,
                params = *arrayOf(
                    filterActiveButtonsNoActive,
                    _dataFilterButtons.value?.initFilterList
                )

            )
        }
    }


    private fun handleProductListResult(products: List<Product>) {
        _productsFiltered.value = ProductContent(productList = products)
    }

    private fun handleInitFilteringButtonsResult(filterButtons: List<Filter>) {
        _dataFilterButtons.value = FilteringModel(
            initFilterList = filterButtons,
            filteredButtons = filterButtons
        )
    }

    private fun handleFilteredButtons(filterButtons: List<Filter>) {
        _dataFilterButtons.value = FilteringModel(
            initFilterList = _dataFilterButtons.value?.initFilterList,
            filteredButtons = filterButtons
        )
    }

    private fun switchActiveFieldOnFilterInitList(filter: Filter) {
        _dataFilterButtons.value?.initFilterList?.find { it.nameFilter == filter.nameFilter }
            ?.copy(isActive = !filter.isActive)
    }


    private fun setDataProducts(productList: List<Product>) {
        dataProducts = productList
    }

    private fun setFilteredButtonsNoActive(filterButtons: List<Filter>) {
        filterActiveButtonsNoActive = filterButtons
    }



}