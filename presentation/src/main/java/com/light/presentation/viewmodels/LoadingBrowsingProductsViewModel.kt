package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.ProductBrowsing
import com.light.presentation.common.Event
import com.light.usecases.RequestBrowsingProductsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class BrowseFittingViewModel(
    private val requestBrowsingProductsUseCase: RequestBrowsingProductsUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    sealed class UiBrowsingModel {
        data class SuccessRequestStatus(val productBrowsingList: List<ProductBrowsing>) :
            UiBrowsingModel()

        data class ErrorRequestStatus(val message: String) : UiBrowsingModel()
        object LoadingStatus : UiBrowsingModel()
    }

    val modelBrowsingLiveData: LiveData<UiBrowsingModel>
        get() = _modelBrowsingLiveData
    private val _modelBrowsingLiveData = MutableLiveData<UiBrowsingModel>()

    object NavigationToShapeFiltering

    private val _modelNavigationShape = MutableLiveData<Event<NavigationToShapeFiltering>>()
    val modelNavigationShape: LiveData<Event<NavigationToShapeFiltering>>
        get() = _modelNavigationShape

    init {
        onRequestBrowsingProducts()
    }

    private fun onRequestBrowsingProducts() {
        launch {
            _modelBrowsingLiveData.value = UiBrowsingModel.LoadingStatus
            requestBrowsingProductsUseCase.execute(::handleSuccessRequest, ::handleErrorRequest)
        }
    }

    private fun handleSuccessRequest(productBrowsingList: List<ProductBrowsing>) {
        _modelBrowsingLiveData.value =
            UiBrowsingModel.SuccessRequestStatus(productBrowsingList)
    }

    private fun handleErrorRequest(exception: Exception, message: String) {
        _modelBrowsingLiveData.value = UiBrowsingModel.ErrorRequestStatus(message)
    }

    fun onFittingClick(product: ProductBrowsing) {
        //todo
    }

    fun onSearchButtonPressed() {
        _modelNavigationShape.value = Event(NavigationToShapeFiltering)
    }
}

