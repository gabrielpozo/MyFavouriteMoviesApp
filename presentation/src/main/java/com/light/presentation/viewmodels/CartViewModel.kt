package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Operation
import com.light.domain.model.CartItemCount
import com.light.domain.model.Product
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


    private val _modelItemCountRequest = MutableLiveData<RequestModelItemCount>()
    val modelItemCountRequest: LiveData<RequestModelItemCount>
        get() = _modelItemCountRequest


    data class RequestModelItemCount(val itemCount: Event<CartItemCount>)

    private val _modelReload = MutableLiveData<ContentReload>()
    val modelReload: LiveData<ContentReload>
        get() {
            return _modelReload
        }

    data class ContentReload(val shouldReload: Boolean = false)


    private val _modelUrl = MutableLiveData<ContentUrl>()

    data class ContentUrl(val url: String)

    fun onRequestGetItemCount() {
      requestItemsCount()
    }

    fun onCheckReloadCartWebView(shouldReload: Boolean) {
        if (shouldReload || _modelUrl.value?.url?.equals(URL_SUCCESS) == true){
            _modelReload.value = ContentReload(true)
            //todo call get cart item count again here
            requestItemsCount()
            Log.d("Gabriel", "Reloading Web View")
        } else {
            Log.d("Gabriel", "It is not reloading the web View")
        }
    }

    fun onSetWebUrl(url: String) {
        Log.d("Gabriel","URL ON VIEW MODEL: $url")
        _modelUrl.value = ContentUrl(url)
    }

    private fun  requestItemsCount(){
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