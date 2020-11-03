package com.light.finder.di.modules.submodules

import com.light.presentation.viewmodels.FilterViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class FilterModule {

    @Provides
    fun filterViewModel() = FilterViewModel(Dispatchers.Main)
}

@Subcomponent(modules = [(FilterModule::class)])
interface FilterComponent {
    val filterViewModel: FilterViewModel
}