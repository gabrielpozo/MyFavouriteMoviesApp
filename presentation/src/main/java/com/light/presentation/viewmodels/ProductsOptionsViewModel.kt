package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.FilterVariationCF
import com.light.domain.model.Product
import com.light.presentation.common.Event
import com.light.presentation.common.getSelectedProduct
import com.light.presentation.common.setSelectedProduct
import com.light.usecases.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ProductsOptionsViewModel(
    override val uiDispatcher: CoroutineDispatcher,
    private val getWattageVariationsUseCase: GetWattageVariationsUseCase,
    private val getColorVariationsUseCase: GetColorVariationsUseCase,
    private val getFinishVariationsUseCase: GetFinishVariationsUseCase,
    private val getNewCompatibleListUseCase: GetNewCompatibleVariationListUseCase,
    private val getNewIncompatibleListUseCase: GetNewIncompatibleVariationListUseCase,
    private val getconnectivityVariationsUseCase: GetConnectivityVariationsUseCase

    ) :
    BaseViewModel(uiDispatcher) {

    private lateinit var dataProducts: List<Product>

    private val _dataFilterWattageButtons = MutableLiveData<FilteringWattage>()
    val dataFilterWattageButtons: LiveData<FilteringWattage>
        get() {
            return _dataFilterWattageButtons
        }

    private val _dataFilterColorButtons = MutableLiveData<FilteringColor>()
    val dataFilterColorButtons: LiveData<FilteringColor>
        get() {
            return _dataFilterColorButtons
        }


    private val _dataFilterFinishButtons = MutableLiveData<FilteringFinish>()
    val dataFilterFinishButtons: LiveData<FilteringFinish>
        get() {
            return _dataFilterFinishButtons
        }

    private val _dataFilterConnectivityButtons = MutableLiveData<FilteringConnectivity>()
    val dataFilterConnectivityButtons: LiveData<FilteringConnectivity>
        get() {
            return _dataFilterConnectivityButtons
        }

    private val _productSelected = MutableLiveData<ProductSelectedModel>()
    val productSelected: LiveData<ProductSelectedModel>
        get() {
            return _productSelected
        }

    data class FilteringWattage(
        val filteredWattageButtons: List<FilterVariationCF> = emptyList(),
        val isUpdated: Boolean = false
    )

    data class FilteringColor(
        val filteredColorButtons: List<FilterVariationCF> = emptyList(),
        val isUpdated: Boolean = false
    )

    data class FilteringFinish(
        val filteredFinishButtons: List<FilterVariationCF> = emptyList(),
        val isUpdated: Boolean = false
    )

    data class FilteringConnectivity(
        val filteredConnectivityButtons: List<FilterVariationCF> = emptyList(),
        val isUpdated: Boolean = false
    )

    data class ProductSelectedModel(
        val productSelected: Product
    )

    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    class NavigationModel(val categoryProducts: List<Product>)


    fun onRetrieveProductsVariation(categoryProducts: List<Product>) {
        categoryProducts.forEach {
            Log.d(
                "GabrielDebugGuide",
                "WATTAGE AND COLOR: ${it.wattageReplaced} -- ${it.colorCctCode} -- ${it.finish}"
            )
        }
        dataProducts = categoryProducts
        val productSelected = dataProducts.find {
            it.isSelected
        }

        productSelected?.let {
            setProductSelectedOnView(it)
        }
        getFilterVariationList()
    }

    fun onFilterWattageTap(filter: FilterVariationCF) {
        if (!filter.isSelected) {
            if (filter.isAvailable) {
                launch {
                    getNewCompatibleListUseCase.execute(
                        { productList -> handleCompatibleResult(filter, productList) },
                        params = *arrayOf(dataProducts, filter)
                    )
                }
            } else {
                launch {
                    getNewIncompatibleListUseCase.execute(
                        { productList -> handleIncompatibleResult(filter, productList) },
                        params = *arrayOf(dataProducts, filter)
                    )
                }
            }
        }
    }

    private fun setProductSelectedOnView(productSelected: Product?) {
        if (productSelected != null) {
            _productSelected.value = ProductSelectedModel(productSelected)
        }
    }

    fun onFilterColorTap(filterColor: FilterVariationCF) {
        if (!filterColor.isSelected) {
            if (filterColor.isAvailable) {
                launch {
                    getNewCompatibleListUseCase.execute(
                        { productList -> handleCompatibleResult(filterColor, productList) },
                        params = *arrayOf(dataProducts, filterColor)
                    )
                }

            } else {
                launch {
                    getNewIncompatibleListUseCase.execute(
                        { productList -> handleIncompatibleResult(filterColor, productList) },
                        params = *arrayOf(dataProducts, filterColor)
                    )
                }
            }
        }
    }

    fun onFilterFinishTap(filterFinish: FilterVariationCF) {
        if (!filterFinish.isSelected) {
            if (filterFinish.isAvailable) {
                launch {
                    getNewCompatibleListUseCase.execute(
                        { productList -> handleCompatibleResult(filterFinish, productList) },
                        params = *arrayOf(dataProducts, filterFinish)
                    )
                }

            } else {
                launch {
                    getNewIncompatibleListUseCase.execute(
                        { productList -> handleIncompatibleResult(filterFinish, productList) },
                        params = *arrayOf(dataProducts, filterFinish)
                    )
                }
            }
        }
    }

    private fun getFilterVariationList(isAnUpdate: Boolean = false) {
        launch {
            getWattageVariationsUseCase.execute(
                { filterWattageButtons ->
                    handleWattageUseCaseResult(
                        filterWattageButtons,
                        isAnUpdate
                    )
                },
                params = *arrayOf(dataProducts)
            )

            getColorVariationsUseCase.execute(
                { filterColorsButtons ->
                    handleColorUseCaseResult(
                        filterColorsButtons,
                        isAnUpdate
                    )
                },
                params = *arrayOf(dataProducts)
            )

            getFinishVariationsUseCase.execute(
                { filterFinishButtons ->
                    handleFinishUseCaseResult(
                        filterFinishButtons,
                        isAnUpdate
                    )
                },
                params = *arrayOf(dataProducts)
            )

            getconnectivityVariationsUseCase.execute(
                { filterConnectivityButtons ->
                    handleConnectivityUseCaseResult(
                        filterConnectivityButtons,
                        isAnUpdate
                    )
                },
                params = *arrayOf(dataProducts)
            )
        }
    }


    private fun handleIncompatibleResult(filter: FilterVariationCF, newListProduct: List<Product>) {
        dataProducts = newListProduct
        setProductSelectedOnView(filter.setSelectedProduct(dataProducts))
        getFilterVariationList(true)
    }


    private fun handleCompatibleResult(filter: FilterVariationCF, newListProduct: List<Product>) {
        dataProducts = newListProduct
        setProductSelectedOnView(getSelectedProduct(dataProducts))
        getFilterVariationList(true)
    }


    private fun handleWattageUseCaseResult(
        filterWattageButtons: List<FilterVariationCF>,
        isAnUpdate: Boolean = false
    ) {
        _dataFilterWattageButtons.value = FilteringWattage(
            filteredWattageButtons = filterWattageButtons, isUpdated = isAnUpdate
        )
    }


    private fun handleColorUseCaseResult(
        filterColorButtons: List<FilterVariationCF>, isAnUpdate: Boolean = false
    ) {
        _dataFilterColorButtons.value = FilteringColor(
            filteredColorButtons = filterColorButtons,
            isUpdated = isAnUpdate
        )
    }


    private fun handleFinishUseCaseResult(
        filterFinishButtons: List<FilterVariationCF>, isAnUpdate: Boolean = false
    ) {
        _dataFilterFinishButtons.value = FilteringFinish(
            filteredFinishButtons = filterFinishButtons,
            isUpdated = isAnUpdate
        )
    }

    private fun handleConnectivityUseCaseResult(
        filterConnectivityButtons: List<FilterVariationCF>, isAnUpdate: Boolean = false
    ) {
        _dataFilterConnectivityButtons.value = FilteringConnectivity(
            filteredConnectivityButtons = filterConnectivityButtons,
            isUpdated = isAnUpdate
        )
    }
}