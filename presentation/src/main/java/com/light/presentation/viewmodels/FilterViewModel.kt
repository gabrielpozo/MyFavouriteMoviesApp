package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.presentation.common.Event
import kotlinx.coroutines.CoroutineDispatcher

class FilterViewModel(uiDispatcher: CoroutineDispatcher) : BaseViewModel(uiDispatcher) {

    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    class NavigationModel(val filterId: Int)

    fun onFilterClick(filterId: Int) {
        _modelNavigation.value = Event(NavigationModel(filterId))
    }

}