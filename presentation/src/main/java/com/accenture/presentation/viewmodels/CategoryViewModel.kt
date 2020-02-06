package com.accenture.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.accenture.domain.model.Categories
import com.accenture.domain.model.Message
import com.accenture.usecases.GetCategoriesResultUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val getCategoryResultUseCase: GetCategoriesResultUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    sealed class UiModel {
        object Loading : UiModel()
        object RequestMessages : UiModel()
        object Navigation : UiModel()
        data class Content(val messages: List<Message>) : UiModel()
    }

    private fun refresh() {
        _model.value = UiModel.RequestMessages
    }

    fun onRequestCategoriesMessages() {
        launch {
            _model.value = UiModel.Loading
            getCategoryResultUseCase.execute(::handleMessagesResponse, ::handleErrorResponse)
        }
    }

    fun onCategoryClicked(category: Categories) {}

    private fun handleMessagesResponse(messages: List<Message>) {
        _model.value = UiModel.Content(messages)
    }

    private fun handleErrorResponse(throwable: Throwable) {
        Log.d("ege77", throwable.message.toString())
        //TODO
    }

}