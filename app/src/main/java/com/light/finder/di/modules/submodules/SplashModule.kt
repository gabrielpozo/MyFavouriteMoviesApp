package com.light.finder.di.modules.submodules

import com.light.presentation.viewmodels.SplashViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class SplashModule {

    @Provides
    fun splashViewModel() = SplashViewModel()
}

@Subcomponent(modules = [(SplashModule::class)])
interface SplashComponent {
    val splashViewModel: SplashViewModel
}