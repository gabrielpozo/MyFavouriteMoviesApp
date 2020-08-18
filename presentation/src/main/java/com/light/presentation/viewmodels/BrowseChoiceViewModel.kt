package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.FormFactorTypeBaseId
import com.light.presentation.common.Event
import com.light.presentation.common.isProductsChoiceSelected
import com.light.presentation.common.resetChoiceProductList
import com.light.presentation.common.setSelectedProductChoice
import com.light.usecases.RequestBrowsingChoiceUseCase
import kotlinx.coroutines.CoroutineDispatcher

class BrowseChoiceViewModel(
    private val requestBrowsingChoiceUseCase: RequestBrowsingChoiceUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {
    private lateinit var productsChoiceSelected: MutableList<ChoiceBrowsing>

    companion object {
        private const val RESET_BASE_ID = -1
    }

    sealed class UiBrowsingChoiceModel {
        data class SuccessRequestStatus(val productBrowsingList: List<ChoiceBrowsing>) :
            UiBrowsingChoiceModel()

        object LoadingStatus : UiBrowsingChoiceModel()
    }

    val modelBrowsingLiveData: LiveData<UiBrowsingChoiceModel>
        get() = _modelBrowsingLiveData
    private val _modelBrowsingLiveData = MutableLiveData<UiBrowsingChoiceModel>()

    data class NavigationToResults(val productsChoiceSelected: List<ChoiceBrowsing>)

    private val _modelNavigationToResult = MutableLiveData<Event<NavigationToResults>>()
    val modelNavigationToResult: LiveData<Event<NavigationToResults>>
        get() = _modelNavigationToResult

    val modelBottomStatus: LiveData<StatusBottomBar>
        get() = _modelBottomStatus
    private val _modelBottomStatus = MutableLiveData<StatusBottomBar>()

    sealed class StatusBottomBar {
        object ResetChoice : StatusBottomBar()
        object ChoiceClicked : StatusBottomBar()
        object NoButtonsClicked : StatusBottomBar()
    }


    fun onRequestFilteringChoices(productBaseId: FormFactorTypeBaseId) {
        /*launch {
            _modelBrowsingLiveData.value = UiBrowsingChoiceModel.LoadingStatus
            requestBrowsingChoiceUseCase.execute(
                ::handleSuccessRequest,
                productBaseId.id,
                productBaseId.name
            )
        }*/
    }

    fun onResetButtonPressed() {
        productsChoiceSelected.resetChoiceProductList()
        _modelBottomStatus.value = StatusBottomBar.ResetChoice
    }

    private fun handleSuccessRequest(productBrowsingList: List<ChoiceBrowsing>) {
        productsChoiceSelected = productBrowsingList.toMutableList()
        _modelBrowsingLiveData.value =
            UiBrowsingChoiceModel.SuccessRequestStatus(productBrowsingList)
    }

    fun onSearchButtonClicked() {
        if (productsChoiceSelected.isProductsChoiceSelected()) {
            _modelNavigationToResult.value = Event(NavigationToResults(productsChoiceSelected))
        }
    }

    fun onChoiceClick(productChoice: ChoiceBrowsing) {
        productsChoiceSelected.setSelectedProductChoice(productChoice)
        if (productsChoiceSelected.isProductsChoiceSelected()) {
            _modelBottomStatus.value = StatusBottomBar.ChoiceClicked
        } else {
            _modelBottomStatus.value = StatusBottomBar.NoButtonsClicked
        }
    }

    fun onSkipButtonClicked() {
        _modelNavigationToResult.value = Event(NavigationToResults(productsChoiceSelected))
    }
}
