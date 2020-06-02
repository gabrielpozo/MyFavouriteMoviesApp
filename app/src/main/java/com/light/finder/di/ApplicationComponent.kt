package com.light.finder.di

import android.app.Application
import com.light.finder.di.modules.camera.LightFinderComponent
import com.light.finder.di.modules.camera.LightFinderModule
import com.light.finder.di.modules.global.HardwareInstanceComponents
import com.light.finder.di.modules.global.HardwareModule
import com.light.finder.di.modules.submodules.*
import com.light.finder.di.modules.singleton.ApplicationModule
import com.light.finder.di.modules.singleton.DataModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent {

    fun plus(module: SplashModule): SplashComponent
    fun plus(module: LightFinderModule): LightFinderComponent
    fun plus(module: ProductsOptionsModule): ProductsOptionsComponent
    fun plus(module: TermsModule): TermsComponent
    fun plus(module: HardwareModule): HardwareInstanceComponents

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): ApplicationComponent
    }
}