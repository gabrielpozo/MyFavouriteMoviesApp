package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.*
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
    private lateinit var dataFiltersWattage: List<FilterWattage>

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


    /**
     *
     */


    fun onRetrieveProductsVariation(categoryProducts: List<Product>) {
        categoryProducts.forEach {
            Log.d(
                "GabrielDebug",
                "WATTAGE AND COLOR: ${it.wattageReplaced} -- ${it.colorCctCode} -- ${it.finish}"
            )
        }
        dataProducts = categoryProducts
        handleSelectedProduct(dataProducts)
    }

    fun onFilterWattageTap(filter: FilterWattage) {
        if (filter.isAvailable) {
            launch {
                getNewSelectedProduct.execute(
                    { handleSelectedProduct(it, true) },
                    params = *arrayOf(dataProducts, filter)
                )
            }
        }
    }

    fun onFilterColorTap(filter: FilterColor) {
        if (filter.isAvailable) {
            launch {
                getNewSelectedProduct.execute(
                    { handleSelectedProduct(it, true) },
                    params = *arrayOf(dataProducts, filter)
                )
            }
        }
    }

    fun onFilterFinishTap(filter: FilterColor) {
        if (filter.isAvailable) {
            launch {
                getNewSelectedProduct.execute(
                    { handleSelectedProduct(it, true) },
                    params = *arrayOf(dataProducts, filter)
                )
            }
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
                params = *arrayOf(productList)
            )

            getColorVariationsUseCase.execute(
                { filterColorsButtons ->
                    handleColorUseCaseResult(
                        filterColorsButtons,
                        isAnUpdate
                    )
                },
                params = *arrayOf(productList)
            )

            getFinishVariationsUseCase.execute(
                { filterFinishButtons ->
                    handleFinishUseCaseResult(
                        filterFinishButtons,
                        isAnUpdate
                    )
                },
                params = *arrayOf(productList)
            )
        }

    }

    private fun handleWattageUseCaseResult(
        filterWattageButtons: List<FilterWattage>,
        isAnUpdate: Boolean = false
    ) {
        dataFiltersWattage = filterWattageButtons
        /*  filterWattageButtons.forEach {
              if(it.isSelected){
                  Log.d("Gabriel","Filter Selected!! ${it.nameFilter}")
              }
          }*/
        _dataFilterWattageButtons.value = FilteringWattage(
            filteredWattageButtons = dataFiltersWattage, isUpdated = isAnUpdate
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