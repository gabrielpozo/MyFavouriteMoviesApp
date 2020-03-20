package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.*
import com.light.usecases.GetColorVariationsUseCase
import com.light.usecases.GetFinishVariationsUseCase
import com.light.usecases.GetWattageVariationsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ProductsOptionsViewModel(
    override val uiDispatcher: CoroutineDispatcher,
    private val getWattageVariationsUseCase: GetWattageVariationsUseCase,
    private val getColorVariationsUseCase: GetColorVariationsUseCase,
    private val getFinishVariationsUseCase: GetFinishVariationsUseCase
) :
    BaseViewModel(uiDispatcher) {

    private lateinit var dataProducts: List<Product>


    private val _dataFilterWattageButtons = MutableLiveData<FilteringVariation>()
    val dataFilterWattageButtons: LiveData<FilteringVariation>
        get() {
            return _dataFilterWattageButtons
        }

    sealed class FilteringVariation {
        data class FilteringWattage(val filteredWattageButtons: List<FilterWattage> = emptyList()) :
            FilteringVariation()

        data class FilteringColor(val filteredColorButtons: List<FilterColor> = emptyList()) :
            FilteringVariation()

        data class FilteringFinish(val filteredFinishButtons: List<FilterFinish> = emptyList()) :
            FilteringVariation()
    }


    fun onRetrieveProductsVariation(categoryProducts: List<Product>) {
        categoryProducts.forEach {
            Log.d("Gabriel","WATTAGE AND COLOR: ${it.wattageReplaced} -- ${it.colorCctCode} -- ${it.finish}")
        }
        dataProducts = categoryProducts
        launch {
            getWattageVariationsUseCase.execute(
                ::handleWattageUseCaseResult,
                params = *arrayOf(categoryProducts)
            )

            getColorVariationsUseCase.execute(
                ::handleColorUseCaseResult,
                params = *arrayOf(categoryProducts)
            )

            getFinishVariationsUseCase.execute(
                ::handleFinishUseCaseResult,
                params = *arrayOf(categoryProducts)
            )
        }
    }


    private fun handleWattageUseCaseResult(filterWattageButtons: List<FilterWattage>) {
        _dataFilterWattageButtons.value = FilteringVariation.FilteringWattage(
            filteredWattageButtons = filterWattageButtons
        )
    }


    private fun handleColorUseCaseResult(filterColorButtons: List<FilterColor>) {
        _dataFilterWattageButtons.value = FilteringVariation.FilteringColor(
            filteredColorButtons = filterColorButtons
        )
    }


    private fun handleFinishUseCaseResult(filterFinishButtons: List<FilterFinish>) {
        _dataFilterWattageButtons.value = FilteringVariation.FilteringFinish(
            filteredFinishButtons = filterFinishButtons
        )
    }

    fun onFilterWattageTap(filter: FilterWattage) {
        if (filter.isAvailable){
            //TODO all the logic here
            //set new filter to the view with live data
            //(_dataFilterWattageButtons.value as FilteringVariation.FilteringWattage).filteredWattageButtons
            //get the reference of the new product

        }

    }


}