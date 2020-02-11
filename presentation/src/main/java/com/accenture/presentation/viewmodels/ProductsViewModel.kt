package com.accenture.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.accenture.domain.model.Category
import com.accenture.domain.model.Product
import kotlinx.coroutines.CoroutineDispatcher


class ProductsViewModel( uiDispatcher: CoroutineDispatcher): BaseViewModel(uiDispatcher) {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            return _model
        }

    sealed class UiModel {
        data class Content(val products: List<Product>) : UiModel()
    }

    fun onRetrieveProducts(category: Category) {
        _model.value = UiModel.Content(products = category.categoryProducts)
    }

}