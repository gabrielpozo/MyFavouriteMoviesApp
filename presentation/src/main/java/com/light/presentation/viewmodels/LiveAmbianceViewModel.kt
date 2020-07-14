package com.light.presentation.viewmodels

import android.util.Log
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

    private val _modelColorList = MutableLiveData<ContentColors>()
    val modelList: LiveData<ContentColors>
        get() {
            return _modelColorList
        }

    data class ContentColors(val cctList: List<CctType>)

    fun onFilterClick(filter: CctType) {
        Log.d("Gabriel","On Filter Click")
        _model.value = Content(filter)
    }

    fun onRetrieveCctList(cctList: List<CctType>) {
        _modelColorList.value = ContentColors(cctList)
    }

    fun onRetrievingColorSelected(): Int = _model.value?.filter?.id ?: -1


}