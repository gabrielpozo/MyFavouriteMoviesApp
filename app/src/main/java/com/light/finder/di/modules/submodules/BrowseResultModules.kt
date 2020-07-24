package com.light.finder.di.modules.submodules

import com.light.presentation.viewmodels.BrowseResultViewModel
import com.light.presentation.viewmodels.CategoryViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class BrowseResultModule {

    @Provides
    fun browseResultViewModel(
    ) = BrowseResultViewModel(
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(BrowseResultModule::class)])
interface BrowseResultComponent {
    val browseResultViewModel: BrowseResultViewModel
}