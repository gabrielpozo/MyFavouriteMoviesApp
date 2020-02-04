package com.accenture.signify.di

import android.app.Application
import com.accenture.signify.di.modules.ApplicationModule
import com.accenture.signify.di.modules.CategoriesComponent
import com.accenture.signify.di.modules.CategoriesModule
import com.accenture.signify.di.modules.DataModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent {

    fun plus(module: CategoriesModule): CategoriesComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): ApplicationComponent
    }
}