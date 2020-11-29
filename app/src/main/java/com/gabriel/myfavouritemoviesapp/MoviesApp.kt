package com.gabriel.myfavouritemoviesapp

import android.app.Application
import com.gabriel.myfavouritemoviesapp.di.ApplicationComponent
import com.gabriel.myfavouritemoviesapp.di.DaggerApplicationComponent

class MoviesApp : Application() {

    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}