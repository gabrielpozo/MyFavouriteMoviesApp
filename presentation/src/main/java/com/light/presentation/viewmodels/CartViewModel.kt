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
        const val URL_CHECKOUT_PROCESS = "/checkout/cart/"
        const val URL_CHECKOUT_ONE_PAGE = "/checkout/onepage/"
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

    sealed class ContentReload {
        object ContentOnCheckProcess : ContentReload()
        object ContentToBeLoaded : ContentReload()
    }


    private val _modelUrl = MutableLiveData<ContentUrl>()

    data class ContentUrl(val url: String)

    private val _modelNetworkConnection = MutableLiveData<NetworkModel>()
    val modelNetworkConnection: LiveData<NetworkModel>
        get() = _modelNetworkConnection


    sealed class NetworkModel {
        object NetworkOffline : NetworkModel()
        object NetworkOnline : NetworkModel()
    }

    fun onRequestGetItemCount() {
        launch {
            getItemCount.execute(
                ::handleItemCountSuccessResponse
            )
        }
    }

    fun onCheckReloadCartWebView(itemAdded: Boolean) {
        if (_modelUrl.value?.url?.equals(URL_CHECKOUT_PROCESS) == true && !itemAdded) {
            _modelReload.value = ContentReload.ContentOnCheckProcess
        } else if (_modelUrl.value?.url?.equals(URL_CHECKOUT_ONE_PAGE) == true && !itemAdded)
            _modelReload.value = ContentReload.ContentOnCheckProcess
        else {
            _modelReload.value = ContentReload.ContentToBeLoaded
        }
    }

    fun onSetWebUrl(url: String) {
        _modelUrl.value = ContentUrl(url)
        if (_modelUrl.value?.url?.equals(URL_SUCCESS) == true) {
            _modelItemCountRequest.value = CountItemsModel.ClearedBadgeItemCount
        }
    }

    private fun handleItemCountSuccessResponse(cartItemCount: CartItemCount) {
        if (cartItemCount.itemQuantity > 0) {
            _modelItemCountRequest.value =
                CountItemsModel.RequestModelItemCount(Event(cartItemCount))
        } else {
            _modelItemCountRequest.value = CountItemsModel.ClearedBadgeItemCount

        }
    }


    fun onCheckNetworkConnection(status: Boolean?) {
        if (status == false) {
            _modelNetworkConnection.value = NetworkModel.NetworkOffline
        } else {
            _modelNetworkConnection.value = NetworkModel.NetworkOnline
        }
    }


}