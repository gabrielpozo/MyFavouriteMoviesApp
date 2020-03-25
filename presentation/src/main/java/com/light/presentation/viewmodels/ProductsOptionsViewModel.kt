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
            setProductSelectedOnView(it)
        }
        handleSelectedProduct()
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
                    setProductSelectedOnView(it)
                }
            }
            handleSelectedProduct(true)
        } else {
            //TODO

            //1.We get the product selected
            val productSelected = dataProducts.find {
                it.isSelected
            }
            //reset list
            dataProducts.forEach {
                it.isAvailable = false
                it.isSelected = false
            }

            //2. check if there is one option with one of the remain variations
            dataProducts.forEach { product ->
                if (productSelected?.wattageReplaced.toString() == filter.nameFilter) {
                    if (productSelected?.colorCctCode == product.colorCctCode
                        || productSelected?.finish == product.finish
                    ) {
                        handleSelectedProduct(true)
                        return
                    }
                }
            }

            //3. otherwise we get the first product option on the list with this wattage
            dataProducts.find { product ->
                product.wattageReplaced.toString()  == filter.nameFilter
            }.also {
                it?.isSelected = true
            }
            handleSelectedProduct(true)

        }
    }

    fun onDoneButtonClicked() {
        _modelNavigation.value = Event(NavigationModel(dataProducts))

    }

    private fun setProductSelectedOnView(productSelected: Product) {
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
                    setProductSelectedOnView(product)
                }
            }
            handleSelectedProduct(true)
        } else {
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
                    setProductSelectedOnView(it)
                }
            }

            handleSelectedProduct(true)
        } else {
        }
    }

    private fun handleSelectedProduct(isAnUpdate: Boolean = false) {
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
        _dataFilterWattageButtons.value = FilteringWattage(
            filteredWattageButtons = filterWattageButtons, isUpdated = isAnUpdate
        )
    }


    private fun handleColorUseCaseResult(
        filterColorButtons: List<FilterColor>, isAnUpdate: Boolean = false
    ) {
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