package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.ProductBrowsing
import com.light.usecases.RequestBrowsingProductsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.lang.Exception


class FittingBrowsingViewModel(
    private val requestBrowsingProductsUseCase: RequestBrowsingProductsUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    sealed class LoadingBrowsingState {
        data class SuccessRequestStatus(private val productBrowsingList: List<ProductBrowsing>) :
            LoadingBrowsingState()

        data class ErrorRequestStatus(private val message: String) : LoadingBrowsingState()
    }

    val loadingBrowsingLiveData: LiveData<LoadingBrowsingState>
        get() = _loadingBrowsingLiveData
    private val _loadingBrowsingLiveData = MutableLiveData<LoadingBrowsingState>()

    init {
        onRequestBrowsingProducts()
    }

    private fun onRequestBrowsingProducts() {
        launch {
            requestBrowsingProductsUseCase.execute(::onSuccessRequest, ::onErrorRequest)
        }
    }

    private fun onSuccessRequest(productBrowsingList: List<ProductBrowsing>) {
        _loadingBrowsingLiveData.value =
            LoadingBrowsingState.SuccessRequestStatus(productBrowsingList)
    }

    private fun onErrorRequest(exception: Exception, message: String) {
        _loadingBrowsingLiveData.value = LoadingBrowsingState.ErrorRequestStatus(message)
    }

}

