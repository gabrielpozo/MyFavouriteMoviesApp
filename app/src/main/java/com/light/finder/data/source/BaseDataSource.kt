package com.light.finder.data.source

import com.google.gson.JsonParseException
import com.light.data.Result
import com.light.util.NO_CONTENT_CODE
import com.light.util.NO_PRODUCTS_CODE
import com.light.util.SUCCESSFUL_CODE
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.ParseException


abstract class BaseDataSource<Dto, DomainModel> {
    protected suspend fun getResult(
        call: suspend () -> Response<Dto>
    ): Result<DomainModel> = try {
        withContext(Dispatchers.IO) {
            val response = call()
            if (response.isSuccessful) {
                when {
                    response.code() == SUCCESSFUL_CODE -> {
                        Result.success(
                            mapResultToDomainModel(response.body()!!),
                            code = response.code()
                        )
                    }
                    response.code() == NO_CONTENT_CODE -> {
                        Result.success(code = response.code())
                    }
                    response.code() == NO_PRODUCTS_CODE -> {
                        Result.success(
                            mapResultToDomainModel(response.body()!!),
                            code = response.code()
                        )
                    }
                    else -> {
                        Result.success(code = response.code())
                    }
                }
            } else {
                Result.badRequest(code = response.code())
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


    protected abstract fun mapResultToDomainModel(dtoObject: Dto): DomainModel
}