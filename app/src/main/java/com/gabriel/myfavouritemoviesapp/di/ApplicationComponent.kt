package com.gabriel.myfavouritemoviesapp.di

import android.app.Application
import com.gabriel.myfavouritemoviesapp.di.modules.ApplicationModule
import com.gabriel.myfavouritemoviesapp.di.modules.DataModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): ApplicationComponent
    }
}