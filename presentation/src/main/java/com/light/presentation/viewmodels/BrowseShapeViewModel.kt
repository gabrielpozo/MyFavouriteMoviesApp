package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.model.ShapeBrowsing
import com.light.presentation.common.Event
import com.light.usecases.RequestBrowsingShapeUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class BrowseShapeViewModel(
    private val requestBrowsingShapeUseCase: RequestBrowsingShapeUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    companion object {
        private const val RESET_BASE_ID = -1
    }

    sealed class UiBrowsingShapeModel {
        data class SuccessRequestStatus(val productBrowsingList: List<ShapeBrowsing>) :
            UiBrowsingShapeModel()

        object LoadingStatus : UiBrowsingShapeModel()
    }

    val modelBrowsingLiveData: LiveData<UiBrowsingShapeModel>
        get() = _modelBrowsingLiveData
    private val _modelBrowsingLiveData = MutableLiveData<UiBrowsingShapeModel>()

    object NavigationToShapeFiltering

    private val _modelNavigationShape = MutableLiveData<Event<NavigationToShapeFiltering>>()
    val modelNavigationShape: LiveData<Event<NavigationToShapeFiltering>>
        get() = _modelNavigationShape

    val modelBottomStatus: LiveData<StatusBottomBar>
        get() = _modelBottomStatus
    private val _modelBottomStatus = MutableLiveData<StatusBottomBar>()

    sealed class StatusBottomBar {
        object ResetFitting : StatusBottomBar()
        object ShapeClicked : StatusBottomBar()
    }


    fun onRequestFilteringShapes(productBaseId: Int) {
        launch {
            _modelBrowsingLiveData.value = UiBrowsingShapeModel.LoadingStatus
            requestBrowsingShapeUseCase.execute(
                ::handleSuccessRequest,
                productBaseId
            )
        }
    }

    fun onResetButtonPressed() {
        _modelBottomStatus.value = StatusBottomBar.ResetFitting
    }

    private fun handleSuccessRequest(productBrowsingList: List<ShapeBrowsing>) {
        _modelBrowsingLiveData.value =
            UiBrowsingShapeModel.SuccessRequestStatus(productBrowsingList)
    }

    fun onShapeClick(product: ShapeBrowsing) {
        Log.d("Gabriel", "SHAPE: onFittingClick: ${product.isSelected}")
        _modelBottomStatus.value = StatusBottomBar.ShapeClicked
    }

}
