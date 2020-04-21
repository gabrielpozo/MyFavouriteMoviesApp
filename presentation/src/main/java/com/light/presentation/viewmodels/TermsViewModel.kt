package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Category
import com.light.presentation.common.Event
import com.light.usecases.AddToSharedPrefUseCase
import kotlinx.coroutines.CoroutineDispatcher


class TermsViewModel(
    private val saveSharedPrefUseCase: AddToSharedPrefUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {


    private val _modelSharedPref = MutableLiveData<Event<SharedPrefModel>>()
    val modelSharedPref: LiveData<Event<SharedPrefModel>>
        get() = _modelSharedPref


    data class SharedPrefModel(val isAccepted: Boolean)



    fun onSharedPrefSaved(isAccepted: Boolean) {


    }




}