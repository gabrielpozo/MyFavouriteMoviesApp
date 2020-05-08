package com.light.finder.di.modules.submodules


import com.light.presentation.viewmodels.TermsViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class TermsModule {

    @Provides
    fun termsViewModel() = TermsViewModel(Dispatchers.Main)
}

@Subcomponent(modules = [(TermsModule::class)])
interface TermsComponent {
    val termsViewModel: TermsViewModel
}
