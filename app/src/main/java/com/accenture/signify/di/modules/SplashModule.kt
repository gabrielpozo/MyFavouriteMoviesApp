package com.accenture.signify.di.modules

import com.accenture.presentation.viewmodels.SplashViewModel
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