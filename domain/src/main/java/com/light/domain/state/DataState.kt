package com.light.domain.state

sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Empty<T>(val message: String) : DataState<T>()
    data class Error(val errorMessage: String, val cause: Exception? = null) : DataState<Nothing>()
    data class Cancel(val errorMessage: String, val cause: Exception? = null) : DataState<Nothing>()

}
