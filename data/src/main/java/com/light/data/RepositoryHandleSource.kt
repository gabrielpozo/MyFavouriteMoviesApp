package com.light.data

import com.light.domain.state.DataState
import com.light.util.CANCEL_ERROR
import com.light.util.EMPTY_RESPONSE
import com.light.util.GENERAL_ERROR
import com.light.util.NULLABLE_ERROR


suspend fun <T> repositoryHandleSource(
    remoteSourceRequest: suspend () -> Result<T>
): DataState<T> {
    remoteSourceRequest.invoke().also { resultRequest ->
        return when (resultRequest.status) {
            Result.Status.SUCCESS -> {
                resultRequest.let { result ->
                    result.data?.run { DataState.Success(data = this) }
                        ?: DataState.Error(NULLABLE_ERROR)
                }
            }

            Result.Status.EMPTY -> {
                DataState.Empty(resultRequest.message ?: EMPTY_RESPONSE)
            }

            Result.Status.ERROR -> {
                DataState.Error(resultRequest.message ?: GENERAL_ERROR)
            }

            Result.Status.CANCELED -> {
                DataState.Cancel(resultRequest.message ?: CANCEL_ERROR)
            }
        }

    }
}

