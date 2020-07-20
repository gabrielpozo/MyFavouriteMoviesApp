package com.light.data

import com.light.domain.model.ParsingError
import com.light.domain.state.DataState
import com.light.domain.state.DataState.ProductsNotAvailable
import com.light.util.*


suspend fun <T> repositoryCartHandleSource(
    remoteSourceRequest: suspend () -> Result<T>
): DataState<T> {
    remoteSourceRequest.invoke().also { resultRequest ->

        return when (resultRequest.status) {
            Result.Status.SUCCESS -> {
                when (resultRequest.code) {
                    SUCCESSFUL_CODE -> {
                        resultRequest.let { result ->
                            result.data?.run {
                                DataState.Success(data = this)
                            } ?: DataState.Error(NULLABLE_ERROR)
                        }
                    }
                    else -> {
                        DataState.Error(NULLABLE_ERROR)
                    }
                }
            }

            Result.Status.ERROR -> {
                DataState.Error(
                    resultRequest.message ?: GENERAL_ERROR
                )
            }
            else -> {
                DataState.Error(
                    resultRequest.message ?: GENERAL_ERROR
                )
            }

        }
    }
}


suspend fun <T> repositoryLegendHandleSource(
    remoteSourceRequest: suspend () -> Result<T>,
    localPreferenceDataSource: suspend (T) -> Unit
): DataState<T> {
    remoteSourceRequest.invoke().also { resultRequest ->

        return when (resultRequest.status) {
            Result.Status.SUCCESS -> {
                when (resultRequest.code) {
                    SUCCESSFUL_CODE -> {
                        resultRequest.let { result ->
                            result.data?.run {
                                localPreferenceDataSource.invoke(this)
                                DataState.Success(data = this)
                            } ?: DataState.Error(NULLABLE_ERROR)
                        }
                    }
                    else -> {
                        DataState.Error(NULLABLE_ERROR)
                    }
                }
            }

            Result.Status.ERROR -> {
                DataState.Error(
                    resultRequest.message ?: GENERAL_ERROR
                )
            }
            else -> {
                DataState.Error(
                    resultRequest.message ?: GENERAL_ERROR
                )
            }
        }
    }
}


suspend fun <T, A> repositoryLightFinderBusinessModel(
    shouldDoFetchLegendRequest: Boolean,
    legendTagsRemoteRequest: suspend () -> Result<A>,
    mainRemoteRequest: suspend () -> Result<T>,
    saveLegendRequestOnLocal: suspend (A) -> Unit,
    shouldSaveOnLocalDb: Boolean = false,
    saveOnDB: suspend (T) -> Unit = {}
): DataState<T> {
    if (shouldDoFetchLegendRequest) {
        legendTagsRemoteRequest.invoke().also { resultInitialRequest ->
            return when (resultInitialRequest.status) {
                Result.Status.SUCCESS -> {
                    saveLegendRequestOnLocal.invoke(resultInitialRequest.data!!)
                    sendMainRequest(mainRemoteRequest, shouldSaveOnLocalDb, saveOnDB)
                }

                Result.Status.ERROR -> {
                    DataState.Error(
                        resultInitialRequest.message ?: GENERAL_ERROR,
                        isCanceled = resultInitialRequest.isCancelRequest
                    )
                }
                else -> {
                    DataState.Error(
                        resultInitialRequest.message ?: GENERAL_ERROR,
                        isCanceled = resultInitialRequest.isCancelRequest
                    )
                }
            }
        }

    } else {
        return sendMainRequest(mainRemoteRequest, shouldSaveOnLocalDb, saveOnDB)
    }

}


private suspend fun <T> sendMainRequest(
    mainRemoteRequest: suspend () -> Result<T>,
    shouldSaveOnLocalDb: Boolean,
    saveOnDB: suspend (T) -> Unit
): DataState<T> {

    mainRemoteRequest.invoke().also { resultRequest ->
        return when (resultRequest.status) {
            Result.Status.SUCCESS -> {
                when (resultRequest.code) {
                    SUCCESSFUL_CODE -> {
                        resultRequest.let { result ->
                            result.data?.run {
                                if (shouldSaveOnLocalDb) {
                                    saveOnDB.invoke(this)
                                }
                                DataState.Success(data = this)
                            } ?: DataState.Error(NULLABLE_ERROR)
                        }
                    }
                    NO_CONTENT_CODE -> {
                        DataState.Empty(resultRequest.message ?: EMPTY_RESPONSE)
                    }

                    NO_PRODUCTS_CODE -> {
                        resultRequest.data?.run {
                            ProductsNotAvailable(data = this)
                        } ?: DataState.Error(NULLABLE_ERROR)

                    }

                    else -> {
                        DataState.Error(NULLABLE_ERROR)
                    }
                }
            }


            Result.Status.TIME_OUT_ERROR -> {
                DataState.TimeOut(resultRequest.message ?: CANCEL_ERROR)
            }

            Result.Status.PARSE_ERROR -> {
                DataState.Error(
                    resultRequest.message ?: GENERAL_ERROR,
                    cause = ParsingError,
                    isCanceled = resultRequest.isCancelRequest
                )
            }

            Result.Status.ERROR -> {
                DataState.Error(
                    resultRequest.message ?: GENERAL_ERROR,
                    isCanceled = resultRequest.isCancelRequest
                )
            }

        }
    }

}
