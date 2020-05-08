package com.light.finder.di.modules.submodules

import com.light.presentation.viewmodels.AboutViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class AboutModule {

    @Provides
    fun aboutViewModel() = AboutViewModel(
        Dispatchers.Main
    )

}

@Subcomponent(modules = [(AboutModule::class)])
interface AboutComponent {
    val aboutViewModel: AboutViewModel
}