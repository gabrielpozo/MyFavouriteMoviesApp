package com.light.usecases

import com.light.domain.state.DataState

abstract class BaseUseCase<T> {

    suspend fun execute(
        onSuccess: (T) -> Unit,
        onError: (Boolean) -> Unit = {},
        onTimeout: (String) -> Unit = {},
        onEmptyResponse: () -> Unit = {},
        vararg params: Any?
    ) {
        when (val dataState = useCaseExecution(params)) {
            is DataState.Success -> onSuccess.invoke(dataState.data)
            is DataState.Error -> onError.invoke(dataState.isCanceled)
            is DataState.TimeOut -> onTimeout.invoke(dataState.errorMessage)
            is DataState.Empty -> onEmptyResponse.invoke()
        }
    }

    protected abstract suspend fun useCaseExecution(params: Array<out Any?>): DataState<T>
}
