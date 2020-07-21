package com.light.finder.di.modules.submodules

import com.light.domain.BrowseLightBulbsRepository
import com.light.presentation.viewmodels.BrowseFittingViewModel
import com.light.usecases.RequestBrowsingProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers


@Module
class BrowseFittingModule {
    @Provides
    fun requestBrowsingProductsUseCase(browseLightBulbsRepository: BrowseLightBulbsRepository): RequestBrowsingProductsUseCase =
        RequestBrowsingProductsUseCase(browseLightBulbsRepository)

    @Provides
    fun browsingFittingViewModel(requestBrowsingProductsUseCase: RequestBrowsingProductsUseCase): BrowseFittingViewModel =
        BrowseFittingViewModel(requestBrowsingProductsUseCase, Dispatchers.Main)
}

@Subcomponent(modules = [(BrowseFittingModule::class)])
interface BrowsingFittingComponent {
    val browseFittingViewModel: BrowseFittingViewModel
}
