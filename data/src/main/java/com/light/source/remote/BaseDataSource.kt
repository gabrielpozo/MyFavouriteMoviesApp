package com.light.source.remote

import kotlinx.coroutines.Deferred
import com.light.data.Result
import kotlinx.coroutines.CancellationException

abstract class BaseDataSource {

    protected suspend fun <T, D> getResult(
        mapper: (T) -> D,
        call: suspend () -> Deferred<T>
    ): Result<D> = try {
        val response = call().await()
        Result.success(mapper(response))
    } catch (ce: CancellationException) {
        Result.error(ce.message ?: ce.toString(), isCancelRequest = true)
    } catch (e: Exception) {
        Result.error(e.message ?: e.toString())
    }

    private fun <T> error(message: String): Result<T> {
        return Result.error("Network call has failed for a following reason: $message")
    }
}
