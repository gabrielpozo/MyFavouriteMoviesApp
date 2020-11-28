package com.gabriel.myfavouritemoviesapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gabriel.domain.ResourceException
import com.gabriel.domain.model.Movie
import com.gabriel.myfavouritemoviesapp.ui.common.ScopedViewModel
import com.gabriel.usecases.GetMoviesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MoviesViewModel(private val getMoviesUseCase: GetMoviesUseCase, uiDispatcher: CoroutineDispatcher) : ScopedViewModel(uiDispatcher) {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    private val _modelError = MutableLiveData<ResourceErrorModel>()
    val modelError: LiveData<ResourceErrorModel>
        get() = _modelError

    sealed class UiModel {
        object Loading : UiModel()
        data class Content(val movies: List<Movie>) : UiModel()
        data class Navigation(val movie: Movie) : UiModel()
        object RequestMovies : UiModel()
    }

    sealed class ResourceErrorModel {
        data class ShowGeneralError(val message: String?) : ResourceErrorModel()
        object ShowEmptyListError : ResourceErrorModel()
    }

    private fun refresh() {
        _model.value = UiModel.RequestMovies
    }

    fun onRequestPopularMovies() {
        launch {
            _model.value = UiModel.Loading
            getMoviesUseCase.execute(onSuccess = ::handleSuccessMovies, onError = ::handleErrorMovies)
        }
    }

    fun onMovieClicked(movie: Movie) {
        _model.value = UiModel.Navigation(movie)
    }

    private fun handleSuccessMovies(movies: List<Movie>) {
        _model.value = UiModel.Content(movies)
    }

    private fun handleErrorMovies(resourceException: ResourceException?) {
        when (resourceException) {
            is ResourceException.NullOrEmptyResource -> _modelError.value = ResourceErrorModel.ShowEmptyListError
            else -> _modelError.value = ResourceErrorModel.ShowGeneralError(resourceException?.message)
        }
    }
}