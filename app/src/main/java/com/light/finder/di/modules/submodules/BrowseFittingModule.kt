package com.light.finder.di.modules.submodules

import com.light.domain.BrowseLightBulbsRepository
import com.light.presentation.viewmodels.BrowseFittingViewModel
import com.light.usecases.GetFormFactorsEditBrowseUseCase
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
    fun requestFormFactorsEditBrowseUseCase(browseLightBulbsRepository: BrowseLightBulbsRepository): GetFormFactorsEditBrowseUseCase =
        GetFormFactorsEditBrowseUseCase(browseLightBulbsRepository)

    @Provides
    fun browsingFittingViewModel(
        requestBrowsingProductsUseCase: RequestBrowsingFittingsUseCase,
        getFormFactorsEditBrowseUseCase: GetFormFactorsEditBrowseUseCase
    ): BrowseFittingViewModel =
        BrowseFittingViewModel(
            requestBrowsingProductsUseCase,
            getFormFactorsEditBrowseUseCase,
            Dispatchers.Main
        )
}

@Subcomponent(modules = [(BrowseFittingModule::class)])
interface BrowsingFittingComponent {
    val browseFittingViewModel: BrowseFittingViewModel
}
