package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Category
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.Message
import com.light.presentation.common.Event
import com.light.usecases.GetBrowseProductsResultUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class BrowseResultViewModel(
    private val getBrowseProductsResultUseCase: GetBrowseProductsResultUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    private val _model = MutableLiveData<ResultBrowse>()
    val model: LiveData<ResultBrowse>
        get() {
            return _model
        }

    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    private val _modelFilter = MutableLiveData<Event<FilterModel>>()
    val modelFilter: LiveData<Event<FilterModel>>
        get() = _modelFilter

    class NavigationModel(val category: Category)
    class FilterModel(val requestCode: Int)


    sealed class ResultBrowse {
        data class Content(val categories: List<Category>, val message: Message) : ResultBrowse()
        data class NoResult(val message: Message) : ResultBrowse()


    }

    data class Content(val messages: List<Category>, val message: Message)


    fun onCategoryClick(category: Category) {
        _modelNavigation.value = Event(
            NavigationModel(category)
        )
    }

    fun onFilterClick(requestCode: Int) {
        _modelFilter.value = Event(FilterModel(requestCode))
    }

    fun onRetrieveShapeProducts(shapeBrowsingList: ArrayList<ChoiceBrowsing>) {
        launch {
            getBrowseProductsResultUseCase.execute(
                ::handleResultProducts,
                ::handleNoResultProducts,
                shapeBrowsingList
            )
        }
    }

    private fun handleResultProducts(message: Message) {
        _model.value = ResultBrowse.Content(message.categories, message)
    }

    private fun handleNoResultProducts(message: Message) {
        _model.value = ResultBrowse.NoResult(message)
    }
}