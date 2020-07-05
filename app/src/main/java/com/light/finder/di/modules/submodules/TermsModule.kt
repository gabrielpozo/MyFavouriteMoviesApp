package com.light.finder.di.modules.submodules


import com.light.domain.LegendRepository
import com.light.presentation.viewmodels.TermsViewModel
import com.light.usecases.GetLegendUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class TermsModule {
    @Provides
    fun getLegendUseCase(legendRepository: LegendRepository) =
        GetLegendUseCase(legendRepository)


    @Provides
    fun termsViewModel(getLegendUseCase: GetLegendUseCase) =
        TermsViewModel(getLegendUseCase, Dispatchers.Main)
}

@Subcomponent(modules = [(TermsModule::class)])
interface TermsComponent {
    val termsViewModel: TermsViewModel
}
