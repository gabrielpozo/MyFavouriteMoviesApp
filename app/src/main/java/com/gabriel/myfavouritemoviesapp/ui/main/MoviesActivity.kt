package com.gabriel.myfavouritemoviesapp.ui.main

import android.os.Bundle
import com.gabriel.myfavouritemoviesapp.R
import com.gabriel.myfavouritemoviesapp.di.modules.MoviesComponent
import com.gabriel.myfavouritemoviesapp.extensions.getViewModel
import com.gabriel.myfavouritemoviesapp.ui.BaseMoviesActivity

class MoviesActivity : BaseMoviesActivity() {

    private lateinit var component: MoviesComponent

    private val moviesViewModel: MoviesViewModel by lazy { getViewModel { component.movieViewModel } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}