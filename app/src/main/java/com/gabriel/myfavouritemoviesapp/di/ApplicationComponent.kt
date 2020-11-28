package com.gabriel.myfavouritemoviesapp.di

import android.app.Application
import com.gabriel.myfavouritemoviesapp.di.modules.ApplicationModule
import com.gabriel.myfavouritemoviesapp.di.modules.DataModule
import com.gabriel.myfavouritemoviesapp.di.modules.MoviesComponent
import com.gabriel.myfavouritemoviesapp.di.modules.MoviesModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent {

    fun plus(module: MoviesModule): MoviesComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): ApplicationComponent
    }
}