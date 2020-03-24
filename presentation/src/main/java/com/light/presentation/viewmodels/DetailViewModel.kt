package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Cart
import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.presentation.common.Event
import com.light.usecases.GetAddToCartUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class DetailViewModel(
    private val getAddToCart: GetAddToCartUseCase,
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


    data class RequestModelContent(val cartItem: Event<Cart>)

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


}