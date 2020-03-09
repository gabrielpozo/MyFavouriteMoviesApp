package com.light.source.remote

import com.light.data.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception

abstract class BaseDataSource {

    protected suspend fun <T, D> getResult(
        mapper: (T) -> D,
        call: suspend () -> Response<T>
    ): Result<D> = try {
        withContext(Dispatchers.IO) {
            val response = call()
            if (response.isSuccessful) {
                if (response.code() == 204) {
                    Result.success(hasContent = false)
                } else {
                    Result.success(mapper(response.body()!!))
                }

            } else {
                Result.error(response.message())
            }
        }

    } catch (ce: CancellationException) {
        Result.error(ce.message ?: ce.toString(), isCancelRequest = true)
    } catch (e: Exception) {
        Result.error(e.message.toString())
    }

    private fun <T> error(message: String): Result<T> {
        return Result.error(message)
    }
}
