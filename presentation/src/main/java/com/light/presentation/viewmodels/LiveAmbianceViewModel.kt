package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.CctType
import kotlinx.coroutines.CoroutineDispatcher

class LiveAmbianceViewModel(
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {


    private val _model = MutableLiveData<Content>()
    val model: LiveData<Content>
        get() {
            return _model
        }

    data class Content(val filter: CctType)


    fun onFilterClick(filter: CctType) {
        _model.value = Content(filter)
    }


}