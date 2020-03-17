package com.light.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.light.domain.model.Category
import com.light.domain.model.Product
import com.light.usecases.GetDetailUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class DetailViewModel(
    private val getDetailsUseCase: GetDetailUseCase,
    uiDispatcher: CoroutineDispatcher
) : BaseViewModel(uiDispatcher) {


    private val _model = MutableLiveData<Content>()
    val model: LiveData<Content>
        get() {
            return _model
        }

    data class Content(val product: Product)

    fun onRetrieveProduct(category: Category) {
        //TODO we just take the first product for now
        _model.value = Content(category.categoryProducts[0])


    }


}