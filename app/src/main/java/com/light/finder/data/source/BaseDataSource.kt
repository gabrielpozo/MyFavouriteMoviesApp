package com.light.finder.data.source

import com.google.gson.JsonParseException
import com.light.data.Result
import com.light.util.IMMERSIVE_FLAG_TIMEOUT
import com.light.util.NO_CONTENT_CODE
import com.light.util.NO_PRODUCTS_CODE
import kotlinx.coroutines.*
import retrofit2.Response
import java.text.ParseException

abstract class BaseDataSource {

    protected suspend fun <T, D> getResult(
        mapper: (T) -> D,
        call: suspend () -> Response<T>
    ): Result<D> = try {
        withContext(Dispatchers.IO) {
            withTimeout(IMMERSIVE_FLAG_TIMEOUT) {
                val response = call()
                if (response.isSuccessful) {
                    when {
                        response.code() == NO_CONTENT_CODE -> {
                            Result.success(code = NO_CONTENT_CODE)
                        }
                        response.code() == NO_PRODUCTS_CODE -> {
                            Result.success(mapper(response.body()!!), code = NO_PRODUCTS_CODE)
                        }
                        else -> {
                            Result.success(mapper(response.body()!!))
                        }
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
    } catch (pe: ParseException) {
        Result.error(pe.message.toString())
    } catch (pe: JsonParseException) {
        Result.error(pe.message ?: pe.toString(), isParseError = true)
    } catch (e: Exception) {
        Result.error(e.message.toString())
    }

    private fun <T> error(message: String): Result<T> {
        return Result.error(message)
    }
}
