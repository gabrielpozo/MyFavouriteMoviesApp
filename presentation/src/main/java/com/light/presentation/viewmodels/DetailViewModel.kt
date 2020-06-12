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

    /**
     * observable product page variables
     */

    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    private val _model = MutableLiveData<Content>()
    val model: LiveData<Content>
        get() {
            return _model
        }

    private val _modelDialog = MutableLiveData<Event<ServerError>>()
    val modelDialog: LiveData<Event<ServerError>>
        get() = _modelDialog

    private val _modelRequest = MutableLiveData<RequestModelContent>()
    val modelRequest: LiveData<RequestModelContent>
        get() = _modelRequest

    private val _modelItemCountRequest = MutableLiveData<RequestModelItemCount>()
    val modelItemCountRequest: LiveData<RequestModelItemCount>
        get() = _modelItemCountRequest
    /**
     * observable variation variables
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


    /***
     * product page data classes
     */
    class NavigationModel(val productList: List<Product>)

    data class RequestModelContent(val cartItem: Event<Cart>)

    data class RequestModelItemCount(val itemCount: Event<CartItemCount>)

    data class Content(val product: Product, val isSingleProduct: Boolean = false)

    object ServerError
    /***
     * variation data classes
     */

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

    fun onRetrieveProduct(products: List<Product>) {
        if (::dataProductsVariation.isInitialized) {
            _model.value = Content(
                products[0].also { it.isSelected = true },
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
        _modelDialog.value = Event(ServerError)
    }

    private fun handleItemCountSuccessResponse(cartItemCount: CartItemCount) {
        _modelItemCountRequest.value = RequestModelItemCount(Event(cartItemCount))
    }


    private fun isSingleProduct(): Boolean = dataProductsVariation.toHashSet().size == 1

    /***
     * variation controller methods
     */

    fun onRetrieveProductsVariation(categoryProducts: List<Product>) {
        dataProductsVariation = categoryProducts
        //TODO(change the name of this method
        onRetrieveProduct(dataProductsVariation)
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
        dataProductsVariation = newListProduct
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