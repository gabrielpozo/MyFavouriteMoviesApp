package com.light.finder.di

import android.app.Application
import com.light.finder.di.modules.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent {

    fun plus(module: SplashModule): SplashComponent
    fun plus(module: LightFinderModule): LightFinderComponent
    fun plus(module: ProductsOptionsModule): ProductsOptionsComponent


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): ApplicationComponent
    }
}