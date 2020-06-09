package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.*
import com.light.presentation.common.Event
import com.light.presentation.common.getSelectedProduct
import com.light.presentation.common.setSelectedProduct
import com.light.usecases.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class DetailViewModel(
    private val getAddToCart: GetAddToCartUseCase,
    private val getItemCount: GetItemCountUseCase,
    private val getWattageVariationsUseCase: GetWattageVariationsUseCase,
    private val getColorVariationsUseCase: GetColorVariationsUseCase,
    private val getFinishVariationsUseCase: GetFinishVariationsUseCase,
    private val getNewCompatibleListUseCase: GetNewCompatibleVariationListUseCase,
    private val getNewIncompatibleListUseCase: GetNewIncompatibleVariationListUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    private lateinit var dataProducts: List<Product>

    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    class NavigationModel(val productList: List<Product>)


    private val _model = MutableLiveData<Content>()
    val model: LiveData<Content>
        get() {
            return _model
        }

    private val _modelDialog = MutableLiveData<Event<DialogModel>>()
    val modelDialog: LiveData<Event<DialogModel>>
        get() = _modelDialog

    sealed class DialogModel {
        object TimeOutError : DialogModel()
        object NotBulbIdentified : DialogModel()
        object ServerError : DialogModel()
    }

    private val _modelRequest = MutableLiveData<RequestModelContent>()
    val modelRequest: LiveData<RequestModelContent>
        get() = _modelRequest

    private val _modelItemCountRequest = MutableLiveData<RequestModelItemCount>()
    val modelItemCountRequest: LiveData<RequestModelItemCount>
        get() = _modelItemCountRequest


    data class RequestModelContent(val cartItem: Event<Cart>)

    data class RequestModelItemCount(val itemCount: Event<CartItemCount>)

    data class Content(val product: Product, val isSingleProduct: Boolean = false)

    fun onRetrieveProduct(category: Category) {
        if (!::dataProducts.isInitialized) {
            dataProducts = category.categoryProducts
            _model.value = Content(
                category.categoryProducts[0].also { it.isSelected = true },
                isSingleProduct = isSingleProduct()
            )
        }
    }

    fun onRequestAddToCart(productSapId: String) {
        if (productSapId.isNotEmpty()) {
            launch {
                getAddToCart.execute(
                    ::handleSuccessResponse,
                    ::handleErrorResponse,
                    ::handleTimeOutResponse,
                    ::handleEmptyResponse,
                    params = *arrayOf(productSapId)
                )
            }
        } else {
            Log.e("ege", "Sap id is empty")
        }
    }

    fun onRequestGetItemCount() {
        launch {
            getItemCount.execute(
                ::handleItemCountSuccessResponse
            )
        }
    }

    private fun handleSuccessResponse(cartItem: Cart) {
        _modelRequest.value = RequestModelContent(Event(cartItem))
    }

    private fun handleErrorResponse(
        hasBeenCancelled: Boolean,
        exception: Exception?,
        message: String
    ) {
        _modelDialog.value = Event(DialogModel.ServerError)
    }

    private fun handleEmptyResponse() {
        _modelDialog.value = Event(DialogModel.NotBulbIdentified)
    }

    private fun handleTimeOutResponse(message: String) {
        _modelDialog.value = Event(DialogModel.TimeOutError)
    }

    private fun handleItemCountSuccessResponse(cartItemCount: CartItemCount) {
        _modelItemCountRequest.value = RequestModelItemCount(Event(cartItemCount))
    }

    private fun handleItemCountErrorResponse(hasBeenCancelled: Boolean) {
        _modelDialog.value = Event(DialogModel.ServerError)
    }

    private fun handleItemCountEmptyResponse() {
        _modelDialog.value = Event(DialogModel.NotBulbIdentified)
    }

    private fun handleItemCountTimeOutResponse(message: String) {
        _modelDialog.value = Event(DialogModel.TimeOutError)
    }

    fun onRetrieveListFromProductVariation(productList: List<Product>) {
        dataProducts = productList
        val productSelected = dataProducts.find { it.isSelected }
        if (productSelected != null) {
            _model.value =
                Content(productSelected)
        }
    }

    fun onChangeVariationClick() {
        _modelNavigation.value = Event(NavigationModel(dataProducts))
    }

    private fun isSingleProduct(): Boolean = dataProducts.toHashSet().size == 1

    /***
     * New Variations
     */
    private lateinit var dataProductsVariation: List<Product>

    private val _dataFilterWattageButtons = MutableLiveData<FilteringWattage>()
    val dataFilterWattageButtons: LiveData<FilteringWattage>
        get() {
            return _dataFilterWattageButtons
        }

    private val _dataFilterColorButtons = MutableLiveData<FilteringColor>()
    val dataFilterColorButtons: LiveData<FilteringColor>
        get() {
            return _dataFilterColorButtons
        }


    private val _dataFilterFinishButtons = MutableLiveData<FilteringFinish>()
    val dataFilterFinishButtons: LiveData<FilteringFinish>
        get() {
            return _dataFilterFinishButtons
        }

    private val _productSelected = MutableLiveData<ProductSelectedModel>()
    val productSelected: LiveData<ProductSelectedModel>
        get() {
            return _productSelected
        }

    data class FilteringWattage(
        val filteredWattageButtons: List<FilterVariationCF> = emptyList(),
        val isUpdated: Boolean = false
    )

    data class FilteringColor(
        val filteredColorButtons: List<FilterVariationCF> = emptyList(),
        val isUpdated: Boolean = false
    )

    data class FilteringFinish(
        val filteredFinishButtons: List<FilterVariationCF> = emptyList(),
        val isUpdated: Boolean = false
    )

    data class ProductSelectedModel(
        val productSelected: Product
    )

/*    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    class NavigationModel(val categoryProducts: List<Product>)*/


    fun onRetrieveProductsVariation(categoryProducts: List<Product>) {
        categoryProducts.forEach {
            Log.d(
                "GabrielDebugGuide",
                "WATTAGE AND COLOR: ${it.wattageReplaced} -- ${it.colorCctCode} -- ${it.finish}"
            )
        }
        dataProductsVariation = categoryProducts
        val productSelected = dataProductsVariation.find {
            it.isSelected
        }

        productSelected?.let {
            setProductSelectedOnView(it)
        }
        getFilterVariationList()
    }

    fun onFilterWattageTap(filter: FilterVariationCF) {
        if (!filter.isSelected) {
            if (filter.isAvailable) {
                launch {
                    getNewCompatibleListUseCase.execute(
                        { productList -> handleCompatibleResult(filter, productList) },
                        params = *arrayOf(dataProductsVariation, filter)
                    )
                }
            } else {
                launch {
                    getNewIncompatibleListUseCase.execute(
                        { productList -> handleIncompatibleResult(filter, productList) },
                        params = *arrayOf(dataProductsVariation, filter)
                    )
                }
            }
        }
    }

    private fun setProductSelectedOnView(productSelected: Product?) {
        if (productSelected != null) {
            _productSelected.value = ProductSelectedModel(productSelected)
        }
    }

    fun onFilterColorTap(filterColor: FilterVariationCF) {
        if (!filterColor.isSelected) {
            if (filterColor.isAvailable) {
                launch {
                    getNewCompatibleListUseCase.execute(
                        { productList -> handleCompatibleResult(filterColor, productList) },
                        params = *arrayOf(dataProductsVariation, filterColor)
                    )
                }

            } else {
                launch {
                    getNewIncompatibleListUseCase.execute(
                        { productList -> handleIncompatibleResult(filterColor, productList) },
                        params = *arrayOf(dataProductsVariation, filterColor)
                    )
                }
            }
        }
    }

    fun onFilterFinishTap(filterFinish: FilterVariationCF) {
        if (!filterFinish.isSelected) {
            if (filterFinish.isAvailable) {
                launch {
                    getNewCompatibleListUseCase.execute(
                        { productList -> handleCompatibleResult(filterFinish, productList) },
                        params = *arrayOf(dataProductsVariation, filterFinish)
                    )
                }

            } else {
                launch {
                    getNewIncompatibleListUseCase.execute(
                        { productList -> handleIncompatibleResult(filterFinish, productList) },
                        params = *arrayOf(dataProductsVariation, filterFinish)
                    )
                }
            }
        }
    }

    private fun getFilterVariationList(isAnUpdate: Boolean = false) {
        launch {
            getWattageVariationsUseCase.execute(
                { filterWattageButtons ->
                    handleWattageUseCaseResult(
                        filterWattageButtons,
                        isAnUpdate
                    )
                },
                params = *arrayOf(dataProductsVariation)
            )

            getColorVariationsUseCase.execute(
                { filterColorsButtons ->
                    handleColorUseCaseResult(
                        filterColorsButtons,
                        isAnUpdate
                    )
                },
                params = *arrayOf(dataProductsVariation)
            )

            getFinishVariationsUseCase.execute(
                { filterFinishButtons ->
                    handleFinishUseCaseResult(
                        filterFinishButtons,
                        isAnUpdate
                    )
                },
                params = *arrayOf(dataProductsVariation)
            )
        }
    }


    private fun handleIncompatibleResult(filter: FilterVariationCF, newListProduct: List<Product>) {
        dataProductsVariation = newListProduct
        setProductSelectedOnView(filter.setSelectedProduct(dataProductsVariation))
        getFilterVariationList(true)
    }


    private fun handleCompatibleResult(filter: FilterVariationCF, newListProduct: List<Product>) {
        dataProducts = newListProduct
        setProductSelectedOnView(getSelectedProduct(dataProductsVariation))
        getFilterVariationList(true)
    }


    private fun handleWattageUseCaseResult(
        filterWattageButtons: List<FilterVariationCF>,
        isAnUpdate: Boolean = false
    ) {
        _dataFilterWattageButtons.value = FilteringWattage(
            filteredWattageButtons = filterWattageButtons, isUpdated = isAnUpdate
        )
    }


    private fun handleColorUseCaseResult(
        filterColorButtons: List<FilterVariationCF>, isAnUpdate: Boolean = false
    ) {
        _dataFilterColorButtons.value = FilteringColor(
            filteredColorButtons = filterColorButtons,
            isUpdated = isAnUpdate
        )
    }


    private fun handleFinishUseCaseResult(
        filterFinishButtons: List<FilterVariationCF>, isAnUpdate: Boolean = false
    ) {
        _dataFilterFinishButtons.value = FilteringFinish(
            filteredFinishButtons = filterFinishButtons,
            isUpdated = isAnUpdate
        )
    }

}