package com.gabriel.myfavouritemoviesapp.ui.main

import com.gabriel.myfavouritemoviesapp.ui.common.ScopedViewModel
import com.gabriel.usecases.GetMoviesUseCase
import kotlinx.coroutines.CoroutineDispatcher

class MoviesViewModel(private val getMoviesUseCase: GetMoviesUseCase, uiDispatcher: CoroutineDispatcher) : ScopedViewModel(uiDispatcher) {
}