package com.light.source.remote

import com.light.data.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

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

    } catch (io: CancellationException) {
        Result.error(io.message ?: io.toString(), isCanceled = true)
    }
    catch (st: SocketTimeoutException) {
        Result.error(st.message ?: st.toString(), isTimeout = true)
    } catch (e: Exception) {
        Result.error(e.message.toString())
    }

    private fun <T> error(message: String): Result<T> {
        return Result.error(message)
    }
}
