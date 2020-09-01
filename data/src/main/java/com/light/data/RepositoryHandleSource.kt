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

            Result.Status.BAD_REQUEST -> {
                DataState.BadRequest(resultRequest.code)
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
    saveLegendRequestOnLocal: suspend (A) -> Unit
): DataState<T> {
    if (shouldDoFetchLegendRequest) {
        legendTagsRemoteRequest.invoke().also { resultInitialRequest ->
            return when (resultInitialRequest.status) {
                Result.Status.SUCCESS -> {
                    saveLegendRequestOnLocal.invoke(resultInitialRequest.data!!)
                    sendMainRequest(mainRemoteRequest)
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
        return sendMainRequest(mainRemoteRequest)
    }

}


private suspend fun <T> sendMainRequest(
    mainRemoteRequest: suspend () -> Result<T>,
    saveOnDB: suspend (T) -> Unit = {}
): DataState<T> {

    mainRemoteRequest.invoke().also { resultRequest ->
        return when (resultRequest.status) {
            Result.Status.SUCCESS -> {
                when (resultRequest.code) {
                    SUCCESSFUL_CODE -> {
                        resultRequest.let { result ->
                            result.data?.run {
                                if (saveOnDB != {}) {
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

            else -> {
                DataState.Error(NULLABLE_ERROR)
            }

        }

    }

}


suspend fun <T, A, U> repositoryBrowsingBusinessModel(
    shouldDoFetchLegendRequest: Boolean,
    legendTagsRemoteRequest: suspend () -> Result<A>,
    mainRemoteRequest: suspend () -> Result<T>,
    saveLegendRequestOnLocal: suspend (A) -> Unit,
    saveBrowsingonLocal: suspend (T) -> Unit,
    legendParsing: () -> U
): DataState<U> {
    if (shouldDoFetchLegendRequest) {
        legendTagsRemoteRequest.invoke().also { resultInitialRequest ->
            return when (resultInitialRequest.status) {
                Result.Status.SUCCESS -> {
                    saveLegendRequestOnLocal.invoke(resultInitialRequest.data!!)
                    sendMainRequestBrowsing(mainRemoteRequest, saveBrowsingonLocal, legendParsing)
                }

                Result.Status.ERROR -> {
                    DataState.Error(
                        resultInitialRequest.message ?: GENERAL_ERROR,
                        isCanceled = resultInitialRequest.isCancelRequest
                    )
                }

                Result.Status.TIME_OUT_ERROR -> {
                    DataState.TimeOut(resultInitialRequest.message ?: CANCEL_ERROR)
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
        return sendMainRequestBrowsing(mainRemoteRequest, saveBrowsingonLocal, legendParsing)
    }

}

//TODO(make this method general for lightfinder and browsing
private suspend fun <T, U> sendMainRequestBrowsing(
    mainRemoteRequest: suspend () -> Result<T>,
    saveOnDB: suspend (T) -> Unit = {},
    legendParsing: () -> U
): DataState<U> {

    mainRemoteRequest.invoke().also { resultRequest ->
        return when (resultRequest.status) {
            Result.Status.SUCCESS -> {
                when (resultRequest.code) {
                    SUCCESSFUL_CODE -> {
                        resultRequest.let { result ->
                            result.data?.run {
                                if (saveOnDB != {}) {
                                    saveOnDB.invoke(this)
                                }
                                DataState.Success(legendParsing())
                            } ?: DataState.Error(NULLABLE_ERROR)
                        }
                    }
                    NO_CONTENT_CODE -> {
                        DataState.Empty(resultRequest.message ?: EMPTY_RESPONSE)
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

            else -> {
                DataState.Error(
                    resultRequest.message ?: GENERAL_ERROR,
                    isCanceled = resultRequest.isCancelRequest
                )
            }

        }
    }

}