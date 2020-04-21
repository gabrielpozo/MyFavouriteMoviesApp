package com.light.usecases

import com.light.domain.state.DataState
import java.lang.Exception

abstract class BaseUseCase<T> {

    suspend fun execute(
        onSuccess: (T) -> Unit,
        onError: (Boolean, Exception?, message: String) -> Unit = { _, _, _ -> },
        onTimeout: (String) -> Unit = {},
        onEmptyResponse: () -> Unit = {},
        vararg params: Any?
    ) {
        when (val dataState = useCaseExecution(params)) {
            is DataState.Success -> onSuccess.invoke(dataState.data)
            is DataState.Error -> onError.invoke(dataState.isCanceled, dataState.cause, dataState.errorMessage)
            is DataState.TimeOut -> onTimeout.invoke(dataState.errorMessage)
            is DataState.Empty -> onEmptyResponse.invoke()
        }
    }

    protected abstract suspend fun useCaseExecution(params: Array<out Any?>): DataState<T>
}
