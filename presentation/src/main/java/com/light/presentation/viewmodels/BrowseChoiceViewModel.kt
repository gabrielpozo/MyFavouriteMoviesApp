package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.ShapeBrowsing
import com.light.presentation.common.*
import com.light.usecases.GetChoiceEditBrowseUseCase
import com.light.usecases.RequestBrowsingChoiceUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class BrowseChoiceViewModel(
    private val requestBrowsingChoiceUseCase: RequestBrowsingChoiceUseCase,
    private val getChoiceBrowsingUseCase: GetChoiceEditBrowseUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {
    private lateinit var productChoiceSelectedList: MutableList<ChoiceBrowsing>
    private var productShapeSelectedList: MutableList<ShapeBrowsing>? = null
    private var baseFormFactorId = -1
    private var baseFormFactorName: String? = null

    sealed class UiBrowsingChoiceModel {
        data class SuccessRequestStatus(val productBrowsingList: List<ChoiceBrowsing>) :
            UiBrowsingChoiceModel()

        object LoadingStatus : UiBrowsingChoiceModel()
    }

    val modelChoiceLiveData: LiveData<UiBrowsingChoiceModel>
        get() = _modelChoiceLiveData
    private val _modelChoiceLiveData = MutableLiveData<UiBrowsingChoiceModel>()

    data class NavigationToResults(
        val productsChoiceSelected: List<ChoiceBrowsing>,
        val productsShapeSelected: List<ShapeBrowsing>?,
        val formFactorId: Int,
        val formFactorName: String?
    )

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

    fun onResetButtonPressed() {
        productChoiceSelectedList.resetChoiceProductList()
        _modelBottomStatus.value = StatusBottomBar.ResetChoice
    }


    fun onRetrieveShapeProducts(
        shapeBrowsingList: ArrayList<ShapeBrowsing>,
        formFactorId: Int,
        formFactorName: String?
    ) {
        baseFormFactorId = formFactorId
        baseFormFactorName = formFactorName
        productShapeSelectedList = shapeBrowsingList
        requestBrowsingChoiceUseCase.execute(::handleSuccessChoiceResults, shapeBrowsingList)
    }

    fun onRetrieveChoiceProducts() {
        launch {
            val choiceBrowsingList = getChoiceBrowsingUseCase.execute()
            handleSuccessChoiceResults(choiceBrowsingList)
            val choiceCategory = choiceBrowsingList.getChoiceSelected()
            if (choiceCategory == null) {
                _modelBottomStatus.value = StatusBottomBar.ResetChoice
            } else {
                onChoiceClick(choiceCategory)
            }
        }
    }

    private fun handleSuccessChoiceResults(choiceResults: List<ChoiceBrowsing>) {
        productChoiceSelectedList = choiceResults.toMutableList()
        _modelChoiceLiveData.value =
            UiBrowsingChoiceModel.SuccessRequestStatus(productChoiceSelectedList)
    }

    fun onSearchButtonClicked() {
        if (productChoiceSelectedList.isProductsChoiceSelected()) {
            _modelNavigationToResult.value =
                Event(
                    NavigationToResults(
                        productChoiceSelectedList,
                        productShapeSelectedList,
                        baseFormFactorId,
                        baseFormFactorName
                    )
                )
        }
    }

    fun onChoiceClick(productChoice: ChoiceBrowsing) {
        productChoiceSelectedList.setSelectedProductChoice(productChoice)
        if (productChoiceSelectedList.isProductsChoiceSelected()) {
            _modelBottomStatus.value = StatusBottomBar.ChoiceClicked
        } else {
            _modelBottomStatus.value = StatusBottomBar.NoButtonsClicked
        }
    }

    fun onSkipButtonClicked() {
        _modelNavigationToResult.value =
            Event(
                NavigationToResults(
                    productChoiceSelectedList,
                    productShapeSelectedList,
                    baseFormFactorId,
                    baseFormFactorName
                )
            )
    }


}
