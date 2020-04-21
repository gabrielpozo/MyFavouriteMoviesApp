package com.light.finder.di.modules

import com.light.presentation.viewmodels.TermsViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class TermsModule {

    @Provides
    fun termsViewModel() = TermsViewModel()
}

@Subcomponent(modules = [(TermsModule::class)])
interface TermsComponent {
    val termsViewModel: TermsViewModel
}