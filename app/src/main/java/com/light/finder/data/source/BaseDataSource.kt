package com.light.finder.data.source

import com.google.gson.JsonParseException
import com.light.data.Result
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
                successfulResponse(response)
            } else {
                badRequestResponse(response.code())
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

    protected abstract fun successfulResponse(
        response: Response<Dto>
    ): Result<DomainModel>

    protected abstract fun badRequestResponse(code: Int): Result<DomainModel>
    protected abstract fun mapResultToDomainModel(dtoObject: Dto): DomainModel
}