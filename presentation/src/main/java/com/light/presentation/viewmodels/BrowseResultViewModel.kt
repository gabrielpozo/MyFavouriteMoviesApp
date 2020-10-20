package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Category
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.Message
import com.light.presentation.common.Event
import com.light.usecases.GetBrowseProductsResultUseCase
import com.light.usecases.GetSortCategoriesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class BrowseResultViewModel(
    private val getSortCategoriesUseCase: GetSortCategoriesUseCase,
    private val getBrowseProductsResultUseCase: GetBrowseProductsResultUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {

    private lateinit var messageEdit: Message

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

    private val _modelSort = MutableLiveData<SortModel>()
    val modelSort: LiveData<SortModel>
        get() = _modelSort

    private val _modelEdit = MutableLiveData<EditTextInfo>()
    val modelEdit: LiveData<EditTextInfo>
        get() = _modelEdit

    private var currentCategoriesList = mutableListOf<Category>()

    data class NavigationModel(val category: Category)
    data class FilterModel(val requestCode: Int)
    data class SortModel(val sortId: Int)
    data class EditTextInfo(val message: Message)


    sealed class ResultBrowse {
        data class SortedContent(val categories: List<Category>) : ResultBrowse()
        data class Content(val categories: List<Category>, val message: Message) : ResultBrowse()
        data class NoResult(val message: Message) : ResultBrowse()
    }


    fun onCategoryClick(category: Category) {
        _modelNavigation.value = Event(
            NavigationModel(category)
        )
    }

    fun onFilterClick(requestCode: Int) {
        _modelFilter.value = Event(FilterModel(requestCode))
    }

    fun onSortResults(sortId: Int) {
        _modelSort.value = SortModel(sortId)
    }

    fun onSortCategories(sortId: Int) {
        launch {
            _model.value = ResultBrowse.SortedContent(
                getSortCategoriesUseCase.executeSorting(
                    sortId,
                    currentCategoriesList
                )
            )
        }

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
        //we initialize here the edit text
        messageEdit = message
        currentCategoriesList = message.categories.toMutableList()
        _model.value = ResultBrowse.Content(message.categories, message)
    }

    private fun handleNoResultProducts(message: Message) {
        _model.value = ResultBrowse.NoResult(message)
    }

    fun onEditTextClicked() {
        if (this::messageEdit.isInitialized)
            _modelEdit.value = EditTextInfo(messageEdit)
    }
}