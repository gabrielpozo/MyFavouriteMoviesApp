package com.gabriel.myfavouritemoviesapp.data.source.remote

import com.gabriel.domain.Resource
import com.gabriel.domain.ResourceException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BaseDataSource<Dto, DomainModel> {

    protected suspend fun getResult(
        call: suspend () -> Response<Dto>
    ): Resource<DomainModel> = try {
        withContext(Dispatchers.IO) {
            val response = call()
            if (response.isSuccessful) {
                Resource.success(
                    mapResultToDomainModel(response.body()!!)
                )
            } else {
                // Parse error response if API is ready for it
                Resource.error(ResourceException.ApiError("PopularMovies - API error"))
            }
        }
    } catch (exception: Exception) {
        Resource.error(
            ResourceException.RemoteResponseError("PopularMovies - Exception error", exception)
        )
    }

    protected abstract fun mapResultToDomainModel(dtoObject: Dto): DomainModel
}