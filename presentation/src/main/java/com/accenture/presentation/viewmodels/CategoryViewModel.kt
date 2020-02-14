package com.accenture.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.accenture.domain.model.Category
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
        data class Navigation(val category: Category) : UiModel()
        data class Content(val messages: List<Message>) : UiModel()
    }

    private fun refresh() {
        _model.value = UiModel.RequestMessages
    }

    fun onRequestCategoriesMessages(base64: String) {
        launch {
            _model.value = UiModel.Loading
            getCategoryResultUseCase.execute(::handleMessagesResponse, ::handleErrorResponse, base64)
        }
    }

    private fun handleMessagesResponse(messages: List<Message>) {
        _model.value = UiModel.Content(messages)
    }

    private fun handleErrorResponse(throwable: Throwable) {
        //TODO
    }

}