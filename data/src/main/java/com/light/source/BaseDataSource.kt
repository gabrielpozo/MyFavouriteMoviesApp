package com.light.source

import kotlinx.coroutines.Deferred
import com.light.data.Result

abstract class BaseDataSource {

    protected suspend fun <T, D> getResult(
        mapper: (T) -> D,
        call: suspend () -> Deferred<T>
    ): Result<D> = try {
        val response = call().await()
        Result.success(mapper(response))
    } catch (e: Exception) {
        Result.error(e.message ?: e.toString())
    }

    private fun <T> error(message: String): Result<T> {
        return Result.error("Network call has failed for a following reason: $message")
    }
}
