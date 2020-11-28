package com.gabriel.myfavouritemoviesapp.ui.main

import com.gabriel.myfavouritemoviesapp.uimodels.MovieUI

sealed class UiModel {
    object Loading : UiModel()
    data class Content(val movies: List<MovieUI>) : UiModel()
    data class Navigation(val movie: MovieUI) : UiModel()
    object RequestMovies : UiModel()
}

sealed class ResourceErrorModel {
    data class ShowGeneralError(val message: String?) : ResourceErrorModel()
    object ShowEmptyListError : ResourceErrorModel()
}