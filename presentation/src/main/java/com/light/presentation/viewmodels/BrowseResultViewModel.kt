package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.presentation.common.Event
import kotlinx.coroutines.CoroutineDispatcher

class BrowseResultViewModel(
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    private val _model = MutableLiveData<Content>()
    val model: LiveData<Content>
        get() {
            return _model
        }

    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    class NavigationModel(val category: Category)

    data class Content(val messages: List<Category>, val message: Message)


    fun onCategoryClick(category: Category) {
        _modelNavigation.value = Event(
            NavigationModel(category)
        )
    }

    fun onRetrieveCategories(message: Message) {
        _model.value = Content(message.categories, message)
    }
}