package com.light.presentation.viewmodels

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

    private lateinit var dataProducts: List<Product>

    private val _productDetails = MutableLiveData<List<Product>>()
    val productDetails: LiveData<List<Product>>
        get() {
            return _productDetails
        }


    fun onRetrieveProductsAndFilters(category: Category) {
        setDataProducts(category.categoryProducts)
        launch {
            getDetailsUseCase.execute(
                ::handleDetailResult,
                params = *arrayOf(dataProducts, emptyList())
            )

            handleDetailResult(category.categoryProducts)
        }
    }

    private fun handleDetailResult(categoryProducts: List<Product>) {

        _productDetails.value = categoryProducts

    }

    private fun setDataProducts(productList: List<Product>) {
        dataProducts = productList
    }








}