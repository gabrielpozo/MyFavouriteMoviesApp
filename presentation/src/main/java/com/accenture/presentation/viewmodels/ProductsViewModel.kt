package com.accenture.presentation.viewmodels

import android.util.Log
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
                params = *arrayOf(dataProducts, emptyList())
            )

            handleProductListResult(category.categoryProducts)
        }
    }


    fun onFilterTap(filter: Filter) {
        launch {
            switchActiveFieldOnFilterInitList(filter)
            // we get the PRODUCTS associated with the filters we have
            getProductListFiltered.execute(
                ::handleProductListResult,
                params = *arrayOf(dataProducts, _dataFilterButtons.value?.initFilterList)
            )

            // we get the FILTERS associated with the products already filtered
            getFilterButtonListUseCase.execute(
                ::handleFilteredButton,
                params = *arrayOf(
                    _productsFiltered.value?.productList,
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

    private fun handleFilteredButton(filteredButtons: List<Filter>){
        _dataFilterButtons.value = FilteringModel(
            initFilterList = _dataFilterButtons.value?.initFilterList,
            filteredButtons = filteredButtons
        )
    }


    private fun switchActiveFieldOnFilterInitList(filter: Filter) {
        //val value = filter.copy(isActive = !filter.isActive)
        _dataFilterButtons.value?.initFilterList?.find { it.nameFilter == filter.nameFilter }
            ?.isActive = !filter.isActive

      //  Log.d("Gabriel","filter on Filter tap $filter")

    }


    private fun setDataProducts(productList: List<Product>) {
        dataProducts = productList
    }

}