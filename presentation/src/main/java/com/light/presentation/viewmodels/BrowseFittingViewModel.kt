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
    private lateinit var productFormFactorBaseId: FormFactorTypeBaseId
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

    data class NavigationToShapeFiltering(val productBaseFormFactor: FormFactorTypeBaseId)

    private val _modelNavigationShape = MutableLiveData<Event<NavigationToShapeFiltering>>()
    val modelNavigationShape: LiveData<Event<NavigationToShapeFiltering>>
        get() = _modelNavigationShape


    val modelBottomStatus: LiveData<Event<ResetFitting>>
        get() = _modelBottomStatus
    private val _modelBottomStatus = MutableLiveData<Event<ResetFitting>>()

    object ResetFitting

    val modelFittingClickStatus: LiveData<FittingClicked>
        get() = _modelFittingClickStatus
    private val _modelFittingClickStatus = MutableLiveData<FittingClicked>()
    object FittingClicked

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
        productFormFactorBaseId = product
        productBaseId = product.id
        _modelFittingClickStatus.value = FittingClicked
    }

    fun onNextButtonPressed() {
        if (productBaseId > NO_ITEM_SELECTED && !isNextDisabled)
            _modelNavigationShape.value = Event(NavigationToShapeFiltering(productFormFactorBaseId))
    }
}

