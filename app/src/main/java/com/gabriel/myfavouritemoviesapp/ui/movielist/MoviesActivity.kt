package com.gabriel.myfavouritemoviesapp.ui.movielist

import android.os.Bundle
import androidx.lifecycle.Observer
import com.gabriel.myfavouritemoviesapp.R
import com.gabriel.myfavouritemoviesapp.di.modules.MoviesComponent
import com.gabriel.myfavouritemoviesapp.di.modules.MoviesModule
import com.gabriel.myfavouritemoviesapp.extensions.*
import com.gabriel.myfavouritemoviesapp.ui.general.BaseMoviesActivity
import com.gabriel.myfavouritemoviesapp.ui.moviedetails.DetailMovieActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error_view.*
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

        setUpObservers()
    }

    private fun setUpObservers() {
        moviesViewModel.model.observe(this, Observer(::updateUi))
        moviesViewModel.modelNavigation.observe(this, Observer(::navigateToDetail))
    }

    private fun updateUi(model: UiModel) {
        if (model is UiModel.Loading) progress_bar.visible() else progress_bar.gone()
        when (model) {
            is UiModel.Content -> adapter.movies = model.movies
            is UiModel.RequestMovies -> moviesViewModel.onRequestPopularMovies()
            is UiModel.ShowEmptyListError -> showErrorView()
            is UiModel.ShowGeneralError -> showErrorView()
        }
    }

    private fun showErrorView() {
        progress_bar.gone()
        error_view.visible()
        error_button.setOnClickListener {
            moviesViewModel.onRetryButtonClicked()
            error_view.gone()
        }
    }

    private fun navigateToDetail(model: NavigationModel) {
        startActivity<DetailMovieActivity> {
            putExtra(DetailMovieActivity.MOVIE_EXTRA, model.movie)
        }
    }
}