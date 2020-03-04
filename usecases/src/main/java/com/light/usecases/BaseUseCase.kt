package com.light.usecases

import com.light.domain.state.DataState

abstract class BaseUseCase<T> {

    suspend fun execute(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = {},
        onCancel:(String)-> Unit = {},
        vararg params: Any?
    ) {
        when (val dataState = useCaseExecution(params)) {
            is DataState.Success -> onSuccess.invoke(dataState.data)
            is DataState.Error -> onError.invoke(Throwable(dataState.errorMessage))
            is DataState.Cancel -> onCancel.invoke(dataState.errorMessage)
        }
    }

    protected abstract suspend fun useCaseExecution(params: Array<out Any?>): DataState<T>
}
