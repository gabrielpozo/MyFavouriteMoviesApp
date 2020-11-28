package com.gabriel.myfavouritemoviesapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gabriel.myfavouritemoviesapp.ui.common.ScopedViewModel
import com.gabriel.myfavouritemoviesapp.uimodels.MovieUI
import kotlinx.coroutines.CoroutineDispatcher

class DetailViewModel(uiDispatcher: CoroutineDispatcher) : ScopedViewModel(uiDispatcher) {

    data class ModelMovie(val movie: MovieUI)

    private val _model = MutableLiveData<ModelMovie>()
    val model: LiveData<ModelMovie>
        get() = _model

    fun start(movie: MovieUI?) {
        movie?.let {
            _model.value = ModelMovie(movie)
        }
    }
}