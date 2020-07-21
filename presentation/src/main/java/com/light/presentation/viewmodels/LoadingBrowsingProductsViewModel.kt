package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.ProductBrowsing
import com.light.usecases.RequestBrowsingProductsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class BrowseFittingViewModel(
    private val requestBrowsingProductsUseCase: RequestBrowsingProductsUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    sealed class UiBrowsingModel {
        data class SuccessRequestStatus(private val productBrowsingList: List<ProductBrowsing>) :
            UiBrowsingModel()
        data class ErrorRequestStatus(private val message: String) : UiBrowsingModel()
        object LoadingStatus : UiBrowsingModel()
    }

    val modelBrowsingLiveData: LiveData<UiBrowsingModel>
        get() = _modelBrowsingLiveData
    private val _modelBrowsingLiveData = MutableLiveData<UiBrowsingModel>()

    init {
        onRequestBrowsingProducts()
    }

    private fun onRequestBrowsingProducts() {
        launch {
            _modelBrowsingLiveData.value = UiBrowsingModel.LoadingStatus
            requestBrowsingProductsUseCase.execute(::onSuccessRequest, ::onErrorRequest)
        }
    }

    private fun onSuccessRequest(productBrowsingList: List<ProductBrowsing>) {
        _modelBrowsingLiveData.value =
            UiBrowsingModel.SuccessRequestStatus(productBrowsingList)
    }

    private fun onErrorRequest(exception: Exception, message: String) {
        _modelBrowsingLiveData.value = UiBrowsingModel.ErrorRequestStatus(message)
    }

    fun onFittingClick(product: ProductBrowsing) {
       //todo
    }

}

