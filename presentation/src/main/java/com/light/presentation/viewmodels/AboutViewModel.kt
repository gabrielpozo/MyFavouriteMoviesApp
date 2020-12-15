package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher


class AboutViewModel(
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {


    private val _modelNetworkConnection = MutableLiveData<NetworkModel>()
    val modelNetworkConnection: LiveData<NetworkModel>
        get() = _modelNetworkConnection


    sealed class NetworkModel {
        object NetworkOffline : NetworkModel()
        object NetworkOnline : NetworkModel()
    }


    fun onCheckNetworkConnection(status: Boolean?) {
        if (status == false) {
            _modelNetworkConnection.value = NetworkModel.NetworkOffline
        } else {
            _modelNetworkConnection.value = NetworkModel.NetworkOnline
        }
    }


}