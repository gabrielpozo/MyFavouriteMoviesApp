package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.model.ShapeBrowsing
import com.light.presentation.common.Event
import com.light.presentation.common.isProductsShapeSelected
import com.light.presentation.common.resetShapeProductList
import com.light.presentation.common.setSelectedProductShape
import com.light.usecases.RequestBrowsingShapeUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class BrowseShapeViewModel(
    private val requestBrowsingShapeUseCase: RequestBrowsingShapeUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {
    private lateinit var productsShapeSelected: MutableList<ShapeBrowsing>

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

    data class NavigationToResults(val productsShapeSelected: List<ShapeBrowsing>)

    private val _modelNavigationToResult = MutableLiveData<Event<NavigationToResults>>()
    val modelNavigationToResult: LiveData<Event<NavigationToResults>>
        get() = _modelNavigationToResult

    val modelBottomStatus: LiveData<StatusBottomBar>
        get() = _modelBottomStatus
    private val _modelBottomStatus = MutableLiveData<StatusBottomBar>()

    sealed class StatusBottomBar {
        object ResetShape : StatusBottomBar()
        object ShapeClicked : StatusBottomBar()
        object NoButtonsClicked : StatusBottomBar()
    }


    fun onRequestFilteringShapes(productBaseId: FormFactorTypeBaseId) {
            launch {
                _modelBrowsingLiveData.value = UiBrowsingShapeModel.LoadingStatus
                requestBrowsingShapeUseCase.execute(
                    ::handleSuccessRequest,
                    productBaseId.id,
                    productBaseId.name
                )
            }
    }

    fun onResetButtonPressed() {
        productsShapeSelected.resetShapeProductList()
        _modelBottomStatus.value = StatusBottomBar.ResetShape
    }

    private fun handleSuccessRequest(productBrowsingList: List<ShapeBrowsing>) {
        productsShapeSelected = productBrowsingList.toMutableList()
        _modelBrowsingLiveData.value =
            UiBrowsingShapeModel.SuccessRequestStatus(productBrowsingList)
    }

    fun onSearchButtonClicked() {
        if (productsShapeSelected.isProductsShapeSelected()) {
            Log.d("Gabriel","isProduct is selected: $productsShapeSelected")
            _modelNavigationToResult.value = Event(NavigationToResults(productsShapeSelected))
        }
    }

    fun onShapeClick(productShape: ShapeBrowsing) {
        Log.d("Gabriel","onshapeCLick: ${productShape.isSelected}")
        productsShapeSelected.setSelectedProductShape(productShape)
        if (productsShapeSelected.isProductsShapeSelected()) {
            Log.d("Gabriel","SHAPE CLICKED")
            _modelBottomStatus.value = StatusBottomBar.ShapeClicked
        } else {
            Log.d("Gabriel","SHAPE NO CLICKED")
            _modelBottomStatus.value = StatusBottomBar.NoButtonsClicked
        }
    }

    fun onSkipButtonClicked() {
        _modelNavigationToResult.value = Event(NavigationToResults(productsShapeSelected))
    }
}
