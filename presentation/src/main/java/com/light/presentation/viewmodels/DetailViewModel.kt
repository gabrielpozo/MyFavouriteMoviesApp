package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Cart
import com.light.domain.model.CartItemCount
import com.light.domain.model.Category
import com.light.domain.model.Product
import com.light.presentation.common.Event
import com.light.usecases.GetAddToCartUseCase
import com.light.usecases.GetItemCountUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class DetailViewModel(
    private val getAddToCart: GetAddToCartUseCase,
    private val getItemCount: GetItemCountUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {


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

    data class Content(val product: Product)

    fun onRetrieveProduct(category: Category) {
        //TODO we just take the first product for now
        _model.value = Content(category.categoryProducts[0])
    }

    fun onRequestAddToCart(productSapId: String) {
        if (productSapId.isNotEmpty()) {
            checkCoroutineIsCancelled()
            launch {
                getAddToCart.execute(
                    ::handleSuccessResponse,
                    ::handleErrorResponse,
                    ::handleTimeOutResponse,
                    ::handleEmptyResponse,
                    productSapId
                )
            }
        } else {
            Log.e("ege", "Sap id is empty")
        }

    }

    fun onRequestGetItemCount() {
        checkCoroutineIsCancelled()
        launch {
            getItemCount.execute(
                ::handleItemCountSuccessResponse,
                ::handleItemCountErrorResponse,
                ::handleItemCountTimeOutResponse,
                ::handleItemCountEmptyResponse
            )
        }
    }

    private fun handleSuccessResponse(cartItem: Cart) {
        _modelRequest.value = RequestModelContent(Event(cartItem))
    }

    private fun handleErrorResponse(hasBeenCancelled: Boolean) {
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


}