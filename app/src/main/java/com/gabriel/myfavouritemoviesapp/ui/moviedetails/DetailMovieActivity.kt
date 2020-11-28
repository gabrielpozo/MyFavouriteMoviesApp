package com.gabriel.myfavouritemoviesapp.ui.moviedetails

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.gabriel.myfavouritemoviesapp.R
import com.gabriel.myfavouritemoviesapp.databinding.ActivityDetailMovieBinding
import com.gabriel.myfavouritemoviesapp.di.modules.DetailMovieComponent
import com.gabriel.myfavouritemoviesapp.di.modules.DetailMovieModule
import com.gabriel.myfavouritemoviesapp.extensions.app
import com.gabriel.myfavouritemoviesapp.extensions.getViewModel
import com.gabriel.myfavouritemoviesapp.ui.general.BaseMoviesActivity
import com.gabriel.myfavouritemoviesapp.uimodels.MovieUI

class DetailMovieActivity : BaseMoviesActivity() {

    companion object {
        const val MOVIE_EXTRA = "DetailActivity:movie"
    }

    private lateinit var component: DetailMovieComponent

    private val detailViewModel: DetailViewModel by lazy { getViewModel { component.detailViewModel } }
    private var _binding: ActivityDetailMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_movie)
        component = app.applicationComponent.plus(DetailMovieModule())
        binding.lifecycleOwner = this

        var movieUI: MovieUI? = null
        if (intent.hasExtra(MOVIE_EXTRA)) {
            movieUI = intent.getParcelableExtra(MOVIE_EXTRA)
        }

        setupObservers()
        detailViewModel.onRetrieveMovie(movieUI)
    }

    private fun setupObservers() {
        detailViewModel.model.observe(this, {
            binding.movieUI = it.movie
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}