package com.light.domain.state

sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Empty<T>(val message: String) : DataState<T>()
    data class ProductsNotAvailable<T>(val data: T) : DataState<T>()
    data class Error(
        val errorMessage: String,
        val cause: Exception? = null,
        val isCanceled: Boolean = false
    ) : DataState<Nothing>()

    data class TimeOut(val errorMessage: String, val cause: Exception? = null) :
        DataState<Nothing>()

}

