package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.CartItemCount
import com.light.presentation.common.Event
import com.light.usecases.GetItemCountUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class CartViewModel(
    private val getItemCount: GetItemCountUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {


    private val _modelItemCountRequest = MutableLiveData<RequestModelItemCount>()
    val modelItemCountRequest: LiveData<RequestModelItemCount>
        get() = _modelItemCountRequest


    data class RequestModelItemCount(val itemCount: Event<CartItemCount>)


    fun onRequestGetItemCount() {
        checkCoroutineIsCancelled()
        launch {
            getItemCount.execute(
                ::handleItemCountSuccessResponse
            )
        }
    }

    private fun handleItemCountSuccessResponse(cartItemCount: CartItemCount) {
        _modelItemCountRequest.value = RequestModelItemCount(Event(cartItemCount))
    }
}