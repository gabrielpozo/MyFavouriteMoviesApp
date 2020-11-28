package com.gabriel.myfavouritemoviesapp.data.source.remote

import com.gabriel.myfavouritemoviesapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface TheMovieDbService {

    companion object {
        private const val LOCAL_REGION = "NL"
    }

    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun getPopularMoviesAsync(
        @Query("api_key") apiKey: String = BuildConfig.movie_db_api_key,
        @Query("region") region: String = LOCAL_REGION
    ): Response<MovieDtoResult>
}
