package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.presentation.common.Event
import kotlinx.coroutines.CoroutineDispatcher

class FilterViewModel(uiDispatcher: CoroutineDispatcher) : BaseViewModel(uiDispatcher) {

    private val _filterSelected = MutableLiveData<FilterSelectedModel>()
    val filterSelected: LiveData<FilterSelectedModel>
        get() {
            return _filterSelected
        }

    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    class NavigationModel(val filterId: Int)

    data class FilterSelectedModel(
        val filterSelected: Int
    )


}