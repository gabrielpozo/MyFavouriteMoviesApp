package com.gabriel.myfavouritemoviesapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.gabriel.myfavouritemoviesapp.R
import com.gabriel.myfavouritemoviesapp.di.modules.MoviesComponent
import com.gabriel.myfavouritemoviesapp.di.modules.MoviesModule
import com.gabriel.myfavouritemoviesapp.extensions.app
import com.gabriel.myfavouritemoviesapp.extensions.getViewModel
import com.gabriel.myfavouritemoviesapp.extensions.startActivity
import com.gabriel.myfavouritemoviesapp.ui.BaseMoviesActivity
import com.gabriel.myfavouritemoviesapp.ui.detail.DetailMovieActivity
import com.gabriel.myfavouritemoviesapp.ui.main.MoviesViewModel.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progress_bar.*

class MoviesActivity : BaseMoviesActivity() {

    private lateinit var component: MoviesComponent
    private lateinit var adapter: MoviesAdapter

    private val moviesViewModel: MoviesViewModel by lazy { getViewModel { component.movieViewModel } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component = app.applicationComponent.plus(MoviesModule())
        adapter = MoviesAdapter(moviesViewModel::onMovieClicked)
        rv.adapter = adapter
        moviesViewModel.model.observe(this, Observer(::updateUi))
    }

    private fun updateUi(model: UiModel) {
        progress_bar.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is UiModel.Content -> {
                adapter.movies = model.movies
            }
            is UiModel.Navigation -> startActivity<DetailMovieActivity> {
                putExtra(DetailMovieActivity.MOVIE_EXTRA, model.movie)
            }
            is UiModel.RequestMovies -> moviesViewModel.onRequestPopularMovies()
        }
    }
}