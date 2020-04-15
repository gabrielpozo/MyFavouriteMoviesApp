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

    companion object {
        const val URL_SUCCESS = "/checkout/onepage/success/"
    }


    private val _modelItemCountRequest = MutableLiveData<CountItemsModel>()
    val modelItemCountRequest: LiveData<CountItemsModel>
        get() = _modelItemCountRequest


    sealed class CountItemsModel {
        data class RequestModelItemCount(val itemCount: Event<CartItemCount>) : CountItemsModel()
        object ClearedBadgeItemCount : CountItemsModel()
    }

    private val _modelReload = MutableLiveData<ContentReload>()
    val modelReload: LiveData<ContentReload>
        get() {
            return _modelReload
        }

    data class ContentReload(val shouldReload: Boolean = false)


    private val _modelUrl = MutableLiveData<ContentUrl>()

    data class ContentUrl(val url: String)

    fun onRequestGetItemCount() {
        launch {
            getItemCount.execute(
                ::handleItemCountSuccessResponse
            )
        }
    }

    fun onCheckReloadCartWebView(shouldReload: Boolean) {
        if (shouldReload || _modelUrl.value?.url?.equals(URL_SUCCESS) == true) {
            _modelReload.value = ContentReload(true)
        }
    }

    fun onSetWebUrl(url: String) {
        _modelUrl.value = ContentUrl(url)
        if (_modelUrl.value?.url?.equals(URL_SUCCESS) == true) {
            _modelItemCountRequest.value = CountItemsModel.ClearedBadgeItemCount
        }
    }

    private val _connectionModel = MutableLiveData<InternetConnection>()
    val connectionModel: LiveData<InternetConnection>
        get() {
            return _connectionModel
        }

    object InternetConnection

    fun onInternetConnectionLost() {
        _connectionModel.value = InternetConnection
    }

    private fun handleItemCountSuccessResponse(cartItemCount: CartItemCount) {
        if (cartItemCount.itemQuantity > 0) {
            _modelItemCountRequest.value =
                CountItemsModel.RequestModelItemCount(Event(cartItemCount))
        } else {
            _modelItemCountRequest.value = CountItemsModel.ClearedBadgeItemCount

        }
    }

}