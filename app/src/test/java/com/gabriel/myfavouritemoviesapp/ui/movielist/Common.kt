package com.gabriel.myfavouritemoviesapp.ui.movielist

import com.gabriel.domain.ResourceException
import com.gabriel.myfavouritemoviesapp.uimodels.MovieUI
import org.mockito.invocation.InvocationOnMock

inline fun <reified T : Any> InvocationOnMock.invocationOnSuccess(valueCase: T) {
    (arguments[0] as (T) -> Unit).invoke(valueCase)
}

inline fun <reified T : Any> InvocationOnMock.invocationOnError(valueCase: T) {
    (arguments[1] as (T) -> Unit).invoke(valueCase)
}

//Mocks
val mockedMovieUi = MovieUI(
    0,
    "Title",
    "Overview",
    "01/01/2025",
    "",
    "",
    "EN",
    "Title",
    5.0,
    5.1,
    false
)

val mockedMovieUiList  = listOf<MovieUI>()

//General
const val errorMessage = "Error default message"
val resourceError = ResourceException.ApiError(errorMessage)