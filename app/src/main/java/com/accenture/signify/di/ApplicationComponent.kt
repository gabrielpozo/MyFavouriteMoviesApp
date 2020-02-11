package com.accenture.signify.di

import android.app.Application
import com.accenture.signify.di.modules.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent {

    fun plus(module: CategoriesModule): CategoriesComponent
    fun plus(module: ProductsModule): ProductsComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): ApplicationComponent
    }
}