package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.domain.model.ShapeBrowsing
import com.light.presentation.common.Event
import com.light.usecases.GetBrowseProductsResultUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class BrowseResultViewModel(
    private val getBrowseProductsResultUseCase: GetBrowseProductsResultUseCase,
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

    fun onRetrieveShapeProducts(shapeBrowsingList: ArrayList<ShapeBrowsing>) {
        Log.d("Gabriel", "Shape Products: $shapeBrowsingList")
        launch {
            getBrowseProductsResultUseCase.execute(::handleResultProducts, shapeBrowsingList)
        }
    }

    private fun handleResultProducts(message: Message) {
        //_model.value = Content(message.categories, message)
    }
}