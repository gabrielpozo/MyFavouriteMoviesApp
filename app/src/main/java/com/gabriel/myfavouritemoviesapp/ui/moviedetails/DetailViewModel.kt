package com.gabriel.myfavouritemoviesapp.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gabriel.myfavouritemoviesapp.ui.common.ScopedViewModel
import com.gabriel.myfavouritemoviesapp.uimodels.MovieUI
import kotlinx.coroutines.CoroutineDispatcher

class DetailViewModel(uiDispatcher: CoroutineDispatcher) : ScopedViewModel(uiDispatcher) {

    private val _model = MutableLiveData<ModelMovie>()
    val model: LiveData<ModelMovie>
        get() = _model

    fun onRetrieveMovie(movie: MovieUI?) {
        movie?.let {
            _model.value = ModelMovie(movie)
        }
    }
}