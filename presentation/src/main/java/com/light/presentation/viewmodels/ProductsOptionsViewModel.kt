package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Category
import com.light.domain.model.FilterColor
import com.light.domain.model.FilterFinish
import com.light.domain.model.FilterWattage
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


    fun onRetrieveProductsVariation(category: Category) {
        //setDataProducts(category.categoryProducts)
        launch {
            getWattageVariationsUseCase.execute(
                ::handleWattageUseCaseResult,
                params = *arrayOf(category.categoryProducts)
            )

            getColorVariationsUseCase.execute(
                ::handleColorUseCaseResult,
                params = *arrayOf(category.categoryProducts)
            )

            getFinishVariationsUseCase.execute(
                ::handleFinishUseCaseResult,
                params = *arrayOf(category.categoryProducts)
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


}