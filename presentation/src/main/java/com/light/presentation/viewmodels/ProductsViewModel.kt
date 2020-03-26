package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Category
import com.light.domain.model.FilterVariation
import com.light.domain.model.Product
import com.light.usecases.GetFilterButtonsUseCase
import com.light.usecases.GetProductsFilteredUseCase
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


    private val _dataFilterButtons = MutableLiveData<FilteringModel>()
    val dataFilterButtons: LiveData<FilteringModel>
        get() {
            return _dataFilterButtons
        }


    data class ProductContent(val productList: List<Product>)
    data class FilteringModel(
        val initFilterList: List<FilterVariation>?,
        val filteredButtons: List<FilterVariation> = emptyList()
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


    fun onFilterTap(filter: FilterVariation) {
        launch {
            switchActiveFieldOnFilterInitList(filter)
            getProductListFiltered.execute(
                ::handleProductListResult,
                params = *arrayOf(dataProducts, _dataFilterButtons.value?.initFilterList)
            )

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

    private fun handleInitFilteringButtonsResult(filterButtons: List<FilterVariation>) {
        _dataFilterButtons.value = FilteringModel(
            initFilterList = filterButtons,
            filteredButtons = filterButtons
        )
    }

    private fun handleFilteredButton(filteredButtons: List<FilterVariation>){
        _dataFilterButtons.value = FilteringModel(
            initFilterList = _dataFilterButtons.value?.initFilterList,
            filteredButtons = filteredButtons
        )
    }


    private fun switchActiveFieldOnFilterInitList(filter: FilterVariation) {
        _dataFilterButtons.value?.initFilterList?.find { it.nameFilter == filter.nameFilter }
            ?.isSelected = !filter.isSelected
    }


    private fun setDataProducts(productList: List<Product>) {
        dataProducts = productList
    }

}