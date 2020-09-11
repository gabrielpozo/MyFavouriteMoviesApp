package com.light.usecases

import com.light.domain.state.DataState

abstract class BaseCartUseCase<T> {

    suspend fun execute(
        onSuccess: (T) -> Unit,
        onError: () -> Unit = {},
        onBadRequest: (Int) -> Unit = {},
        vararg params: Any?
    ) {
        when (val dataState = useCaseExecution(params)) {
            is DataState.Success -> onSuccess.invoke(dataState.data)
            is DataState.Error -> onError.invoke()
            is DataState.BadRequest -> onBadRequest.invoke(dataState.code)
        }
    }

    protected abstract suspend fun useCaseExecution(params: Array<out Any?>): DataState<T>
}