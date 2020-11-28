package com.gabriel.myfavouritemoviesapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.covidglobal.general.SingleLiveEvent
import com.gabriel.domain.ResourceException
import com.gabriel.domain.models.Movie
import com.gabriel.myfavouritemoviesapp.ui.common.ScopedViewModel
import com.gabriel.myfavouritemoviesapp.uimodels.MovieUI
import com.gabriel.myfavouritemoviesapp.uimodels.toMovieUIModel
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

    private val _modelError = SingleLiveEvent<ResourceErrorModel>()
    val modelError: LiveData<ResourceErrorModel>
        get() = _modelError

    private fun refresh() {
        _model.value = UiModel.RequestMovies
    }

    fun onRequestPopularMovies() {
        loadMovies()
    }

    fun onRetryButtonClicked() {
        loadMovies()
    }

    fun onMovieClicked(movie: MovieUI) {
        _model.value = UiModel.Navigation(movie)
    }

    private fun loadMovies() {
        launch {
            _model.value = UiModel.Loading
            getMoviesUseCase.execute(
                onSuccess = ::handleSuccessMovies, onError = ::handleErrorMovies
            )
        }
    }

    private fun handleSuccessMovies(movies: List<Movie>) {
        _model.value = UiModel.Content(movies.map { it.toMovieUIModel() })
    }

    private fun handleErrorMovies(resourceException: ResourceException?) {
        when (resourceException) {
            is ResourceException.NullOrEmptyResource -> _modelError.value = ResourceErrorModel.ShowEmptyListError
            else -> _modelError.value = ResourceErrorModel.ShowGeneralError(resourceException?.message)
        }
    }
}