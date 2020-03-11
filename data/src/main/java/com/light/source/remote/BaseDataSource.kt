package com.light.source.remote

import com.light.data.Result
import com.light.util.IMMERSIVE_FLAG_TIMEOUT
import com.light.util.NO_CONTENT_CODE
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception

abstract class BaseDataSource {

    protected suspend fun <T, D> getResult(
        mapper: (T) -> D,
        call: suspend () -> Response<T>
    ): Result<D> = try {
        withContext(Dispatchers.IO) {
            withTimeout(IMMERSIVE_FLAG_TIMEOUT) {
                val response = call()
                if (response.isSuccessful) {
                    if (response.code() == NO_CONTENT_CODE) {
                        Result.success(hasContent = false)
                    } else {
                        Result.success(mapper(response.body()!!))
                    }

                } else {
                    Result.error(response.message())
                }
            }
        }

    } catch (time: TimeoutCancellationException) {
        Result.error(time.message ?: time.toString(), isTimeout = true)
    } catch (io: CancellationException) {
        Result.error(io.message ?: io.toString(), isCanceled = true)
    } catch (e: Exception) {
        Result.error(e.message.toString())
    }

    private fun <T> error(message: String): Result<T> {
        return Result.error(message)
    }
}
