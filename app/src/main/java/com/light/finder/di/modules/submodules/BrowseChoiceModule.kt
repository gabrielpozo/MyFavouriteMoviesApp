package com.light.finder.di.modules.submodules

import com.light.domain.BrowseChoiceRepository
import com.light.presentation.viewmodels.BrowseChoiceViewModel
import com.light.usecases.RequestBrowsingChoiceUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class BrowseChoiceModule {
    @Provides
    fun requestChoiceProductsUseCase(browseLightBulbsRepository: BrowseChoiceRepository): RequestBrowsingChoiceUseCase =
        RequestBrowsingChoiceUseCase(browseLightBulbsRepository)

    @Provides
    fun browsingChoiceViewModel(requestBrowsingProductsUseCase: RequestBrowsingChoiceUseCase): BrowseChoiceViewModel =
        BrowseChoiceViewModel(requestBrowsingProductsUseCase, Dispatchers.Main)
}

@Subcomponent(modules = [(BrowseChoiceModule::class)])
interface BrowseChoiceComponent {
    val browseChoiceViewModel: BrowseChoiceViewModel
}
