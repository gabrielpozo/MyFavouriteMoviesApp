package com.light.finder.di.modules.submodules

import com.light.domain.AuthRepository
import com.light.domain.LegendRepository
import com.light.presentation.viewmodels.SplashViewModel
import com.light.usecases.GetBearerTokenUseCase
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
    fun getBearerTokenUseCase(authRepository: AuthRepository) =
        GetBearerTokenUseCase(authRepository)

    @Provides
    fun splashViewModel(
        getLegendUseCase: GetLegendUseCase,
        getBearerTokenUseCase: GetBearerTokenUseCase
    ) = SplashViewModel(
        getLegendUseCase, getBearerTokenUseCase, Dispatchers.Main
    )
}

@Subcomponent(modules = [(SplashModule::class)])
interface SplashComponent {
    val splashViewModel: SplashViewModel
}