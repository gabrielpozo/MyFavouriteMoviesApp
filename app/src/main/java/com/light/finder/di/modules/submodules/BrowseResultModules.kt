package com.light.finder.di.modules.submodules

import com.light.domain.ProductBrowsingRepository
import com.light.presentation.viewmodels.BrowseResultViewModel
import com.light.usecases.GetBrowseProductsResultUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class BrowseResultModule {

    @Provides
    fun getResultUseCase(productBrowsingRepository: ProductBrowsingRepository) =
        GetBrowseProductsResultUseCase(productBrowsingRepository)


    @Provides
    fun browseResultViewModel(
        getBrowseProductsResultUseCase: GetBrowseProductsResultUseCase
    ) = BrowseResultViewModel(
        getBrowseProductsResultUseCase,
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(BrowseResultModule::class)])
interface BrowseResultComponent {
    val browseResultViewModel: BrowseResultViewModel
}