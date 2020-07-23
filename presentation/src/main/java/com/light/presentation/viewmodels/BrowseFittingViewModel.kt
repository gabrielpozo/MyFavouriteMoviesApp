package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.FormFactorTypeBaseId
import com.light.presentation.common.Event
import com.light.usecases.RequestBrowsingFittingsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class BrowseFittingViewModel(
    private val requestBrowsingProductsUseCase: RequestBrowsingFittingsUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    companion object {
        private const val NO_ITEM_SELECTED = -1
    }

    private var productBaseId: Int = NO_ITEM_SELECTED
    private var isNextDisabled = true


    sealed class UiBrowsingModel {
        data class SuccessRequestStatus(val productBrowsingList: List<FormFactorTypeBaseId>) :
            UiBrowsingModel()

        data class ErrorRequestStatus(val message: String) : UiBrowsingModel()
        object LoadingStatus : UiBrowsingModel()
    }

    val modelBrowsingLiveData: LiveData<UiBrowsingModel>
        get() = _modelBrowsingLiveData
    private val _modelBrowsingLiveData = MutableLiveData<UiBrowsingModel>()

    data class NavigationToShapeFiltering(val productBaseId: Int)

    private val _modelNavigationShape = MutableLiveData<Event<NavigationToShapeFiltering>>()
    val modelNavigationShape: LiveData<Event<NavigationToShapeFiltering>>
        get() = _modelNavigationShape


    val modelReset: LiveData<StatusBottomBar>
        get() = _modelReset
    private val _modelReset = MutableLiveData<StatusBottomBar>()

    sealed class StatusBottomBar {
        object ResetFitting : StatusBottomBar()
        object FittingClicked : StatusBottomBar()

    }

    init {
        onRequestBrowsingProducts()
    }

    fun onRequestBrowsingProducts() {
        launch {
            _modelBrowsingLiveData.value = UiBrowsingModel.LoadingStatus
            requestBrowsingProductsUseCase.execute(::handleSuccessRequest, ::handleErrorRequest)
        }
    }

    private fun handleSuccessRequest(productBrowsingList: List<FormFactorTypeBaseId>) {
        _modelBrowsingLiveData.value =
            UiBrowsingModel.SuccessRequestStatus(productBrowsingList)
    }

    private fun handleErrorRequest(exception: Exception, message: String) {
        _modelBrowsingLiveData.value = UiBrowsingModel.ErrorRequestStatus(message)
    }

    fun onFittingClick(product: FormFactorTypeBaseId) {
        isNextDisabled = false
        productBaseId = product.id
        _modelReset.value = StatusBottomBar.FittingClicked
    }

    fun onNextButtonPressed() {
        if (productBaseId > NO_ITEM_SELECTED && !isNextDisabled)
            _modelNavigationShape.value = Event(NavigationToShapeFiltering(productBaseId))
    }

    fun onResetButtonPressed() {
        isNextDisabled = true
        _modelReset.value = StatusBottomBar.ResetFitting
    }
}

