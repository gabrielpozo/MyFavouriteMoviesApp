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

    private lateinit var dataProducts: List<Product>

    private val _modelNavigation = MutableLiveData<Event<NavigationModel>>()
    val modelNavigation: LiveData<Event<NavigationModel>>
        get() = _modelNavigation

    class NavigationModel(val productList: List<Product>)


    private val _model = MutableLiveData<Content>()
    val model: LiveData<Content>
        get() {
            return _model
        }

    data class Content(val product: Product)

    private val _modelContentVariation = MutableLiveData<ContentVariation>()
    val modelContentVariation: LiveData<ContentVariation>
        get() {
            return _modelContentVariation
        }

    data class ContentVariation(val product: Product)

    fun onRetrieveProduct(category: Category) {
        if (!::dataProducts.isInitialized) {
            dataProducts = category.categoryProducts
            _model.value = Content(category.categoryProducts[0].also { it.isSelected = true })
        }
    }

    fun onRetrieveListFromProductVariation(productList: List<Product>) {
        dataProducts = productList
        val productSelected = dataProducts.find { it.isSelected }
        if (productSelected != null) {
            _modelContentVariation.value = ContentVariation(productSelected)
        }
    }

    fun onChangeVariationClick() {
        _modelNavigation.value = Event(NavigationModel(dataProducts))
    }
}