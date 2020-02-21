package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.presentation.common.Event
import com.light.usecases.GetCategoriesResultUseCase
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

    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    class NavigationModel(val category: Category)

    sealed class UiModel {
        object Loading : UiModel()
        object RequestMessages : UiModel()
        data class Content(val messages: List<Message>) : UiModel()
    }

    private fun refresh() {
        _model.value = UiModel.RequestMessages
    }

    fun onRequestCategoriesMessages(base64: String) {
        launch {
            _model.value = UiModel.Loading
                getCategoryResultUseCase.execute(
                    ::handleMessagesResponse,
                    ::handleErrorResponse,
                    base64
                )
        }
    }

    fun onCategoryClick(category: Category) {
        _modelNavigation.value = Event(
            NavigationModel(category)
        )
    }

    private fun handleMessagesResponse(messages: List<Message>) {
        _model.value = UiModel.Content(messages)
    }

    private fun handleErrorResponse(throwable: Throwable) {}

}