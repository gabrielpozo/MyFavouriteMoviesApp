package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.*
import com.light.presentation.common.Event
import com.light.usecases.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ProductsOptionsViewModel(
    override val uiDispatcher: CoroutineDispatcher,
    private val getWattageVariationsUseCase: GetWattageVariationsUseCase,
    private val getColorVariationsUseCase: GetColorVariationsUseCase,
    private val getFinishVariationsUseCase: GetFinishVariationsUseCase,
    private val getAvailableSelectedFilterUseCase: GetAvailableSelectedFilterUseCase,
    private val getNewSelectedProduct: GetNewSelectedProduct
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

    private val _productSelected = MutableLiveData<ProductSelectedModel>()
    val productSelected: LiveData<ProductSelectedModel>
        get() {
            return _productSelected
        }

    data class FilteringWattage(
        val filteredWattageButtons: List<FilterWattage> = emptyList(),
        val isUpdated: Boolean = false
    )

    data class FilteringColor(
        val filteredColorButtons: List<FilterColor> = emptyList(), val isUpdated: Boolean = false
    )

    data class FilteringFinish(
        val filteredFinishButtons: List<FilterFinish> = emptyList(), val isUpdated: Boolean = false
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
            setProductSelected(it)
        }
        handleSelectedProduct(dataProducts)
    }

    fun onFilterWattageTap(filter: FilterWattage) {
        if (filter.isAvailable) {
            //check for the data products
            val productSelected = dataProducts.find {
                it.isSelected
            }

            dataProducts.forEach {
                it.isAvailable = false
                it.isSelected = false
            }

            dataProducts.forEach {
                if (it.wattageReplaced.toString() == filter.nameFilter
                    && productSelected?.colorCctCode == it.colorCctCode && productSelected.finish == it.finish
                ) {
                    it.isSelected = true
                    setProductSelected(it)
                }
            }
            handleSelectedProduct(dataProducts, true)
        } else {
            Log.d("GabrielTag", "NOT SELECTED!!")
        }
    }

    fun onDoneButtonClicked() {
        _modelNavigation.value = Event(NavigationModel(dataProducts))

    }

    private fun setProductSelected(productSelected: Product) {
        _productSelected.value = ProductSelectedModel(productSelected)
    }

    fun onFilterColorTap(filterColor: FilterColor) {
        if (filterColor.isAvailable) {
            val productSelected = dataProducts.find {
                it.isSelected
            }

            dataProducts.forEach {
                it.isAvailable = false
                it.isSelected = false
            }

            dataProducts.forEach { product ->
                if (product.colorCctCode == filterColor.nameFilter
                    && productSelected?.wattageReplaced.toString() == product.wattageReplaced.toString() && productSelected?.finish == product.finish
                ) {
                    product.isSelected = true
                    setProductSelected(product)
                }
            }
            handleSelectedProduct(dataProducts, true)
        } else {
            Log.d("GabrielTag", "NOT SELECTED!! COLOR")
        }
    }

    fun onFilterFinishTap(filter: FilterFinish) {
        if (filter.isAvailable) {
            val productSelected = dataProducts.find {
                it.isSelected
            }

            dataProducts.forEach {
                it.isAvailable = false
                it.isSelected = false
            }

            dataProducts.forEach {
                if (it.finish == filter.nameFilter
                    && productSelected?.colorCctCode == it.colorCctCode && productSelected.wattageReplaced.toString() == it.wattageReplaced.toString()
                ) {
                    it.isSelected = true
                    setProductSelected(it)
                }
            }

            handleSelectedProduct(dataProducts, true)
        } else {
            Log.d("GabrielTag", "NOT SELECTED!! FINISH")
        }
    }

    private fun handleSelectedProduct(productList: List<Product>, isAnUpdate: Boolean = false) {
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
        }

    }

    private fun handleWattageUseCaseResult(
        filterWattageButtons: List<FilterWattage>,
        isAnUpdate: Boolean = false
    ) {
        //dataFiltersWattage = filterWattageButtons
        filterWattageButtons.forEach {

            Log.d(
                "GabrielDebugV",
                "ViewModel Filter Name!! WATTAGE ${it.nameFilter} Available: ${it.isAvailable} and isSelected ${it.isSelected}"
            )

        }
        _dataFilterWattageButtons.value = FilteringWattage(
            filteredWattageButtons = filterWattageButtons, isUpdated = isAnUpdate
        )
    }


    private fun handleColorUseCaseResult(
        filterColorButtons: List<FilterColor>, isAnUpdate: Boolean = false
    ) {
        filterColorButtons.forEach {
            Log.d(
                "GabrielDebugV",
                "ViewModel Filter Name!! COLOR: ${it.nameFilter}, Available: ${it.isAvailable} and isSelected ${it.isSelected}"
            )

        }

        _dataFilterColorButtons.value = FilteringColor(
            filteredColorButtons = filterColorButtons,
            isUpdated = isAnUpdate
        )
    }


    private fun handleFinishUseCaseResult(
        filterFinishButtons: List<FilterFinish>, isAnUpdate: Boolean = false
    ) {
        _dataFilterFinishButtons.value = FilteringFinish(
            filteredFinishButtons = filterFinishButtons,
            isUpdated = isAnUpdate
        )
    }
}