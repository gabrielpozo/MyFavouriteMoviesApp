package com.accenture.domain.state

sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String, val cause: Exception? = null) : DataState<Nothing>()
}
