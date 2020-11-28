package com.gabriel.myfavouritemoviesapp.ui.movielist

import com.gabriel.myfavouritemoviesapp.uimodels.MovieUI

sealed class UiModel {
    object Loading : UiModel()
    data class Content(val movies: List<MovieUI>) : UiModel()
    object RequestMovies : UiModel()
}

data class NavigationModel(val movie: MovieUI)

sealed class ResourceErrorModel {
    data class ShowGeneralError(val message: String?) : ResourceErrorModel()
    object ShowEmptyListError : ResourceErrorModel()
}