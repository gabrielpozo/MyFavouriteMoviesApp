package com.light.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Category
import com.light.domain.model.Product
import com.light.presentation.common.Event
import com.light.usecases.GetDetailUseCase
import kotlinx.coroutines.CoroutineDispatcher


class DetailViewModel(
    private val getDetailsUseCase: GetDetailUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {


    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    class NavigationModel(val category: Category)


    private val _model = MutableLiveData<Content>()
    val model: LiveData<Content>
        get() {
            return _model
        }

    data class Content(val product: Product)

    fun onRetrieveProduct(category: Category) {
        _model.value = Content(category.categoryProducts[0].also { it.isSelected = true })
    }

    fun onChangeVariationClick(category: Category) {
        _modelNavigation.value = Event(NavigationModel(category))
    }

}