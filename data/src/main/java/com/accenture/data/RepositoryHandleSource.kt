package com.accenture.data

import com.accenture.domain.state.DataState
import com.accenture.util.GENERAL_ERROR
import com.accenture.util.NULLABLE_ERROR


suspend fun <T> repositoryHandleSource(
    remoteSourceRequest: suspend () -> Result<T>
): DataState<T> {
    remoteSourceRequest.invoke().also { resultRemoteRequest ->
        return when (resultRemoteRequest.status) {
            Result.Status.SUCCESS -> {
                resultRemoteRequest.let { result ->
                    result.data?.run { DataState.Success(data = this) }
                        ?: DataState.Error(NULLABLE_ERROR)
                }
            }

            Result.Status.ERROR -> {
                DataState.Error(resultRemoteRequest.message ?: GENERAL_ERROR)
            }
        }

    }
}

