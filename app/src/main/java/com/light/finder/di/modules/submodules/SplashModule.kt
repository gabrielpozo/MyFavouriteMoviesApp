package com.light.finder.di.modules.submodules

import com.light.domain.LegendRepository
import com.light.presentation.viewmodels.SplashViewModel
import com.light.usecases.GetLegendUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class SplashModule {
    @Provides
    fun getLegendUseCase(legendRepository: LegendRepository) =
        GetLegendUseCase(legendRepository)

    @Provides
    fun splashViewModel(getLegendUseCase: GetLegendUseCase) = SplashViewModel(
        getLegendUseCase, Dispatchers.Main
    )
}

@Subcomponent(modules = [(SplashModule::class)])
interface SplashComponent {
    val splashViewModel: SplashViewModel
}