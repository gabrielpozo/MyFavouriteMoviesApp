package com.light.finder.di.modules.submodules

import com.light.domain.BrowseLightBulbsRepository
import com.light.presentation.viewmodels.BrowseFittingViewModel
import com.light.usecases.RequestBrowsingFittingsUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers


@Module
class BrowseFittingModule {
    @Provides
    fun requestBrowsingProductsUseCase(browseLightBulbsRepository: BrowseLightBulbsRepository): RequestBrowsingFittingsUseCase =
        RequestBrowsingFittingsUseCase(browseLightBulbsRepository)

    @Provides
    fun browsingFittingViewModel(requestBrowsingProductsUseCase: RequestBrowsingFittingsUseCase): BrowseFittingViewModel =
        BrowseFittingViewModel(requestBrowsingProductsUseCase, Dispatchers.Main)
}

@Subcomponent(modules = [(BrowseFittingModule::class)])
interface BrowsingFittingComponent {
    val browseFittingViewModel: BrowseFittingViewModel
}
