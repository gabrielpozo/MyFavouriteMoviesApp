package com.light.finder.di.modules.submodules

import com.light.domain.ShapeLightBulbRepository
import com.light.presentation.viewmodels.BrowseShapeViewModel
import com.light.usecases.RequestBrowsingShapeUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class BrowseShapeModule {
    @Provides
    fun requestShapeProductsUseCase(browseLightBulbsRepository: ShapeLightBulbRepository): RequestBrowsingShapeUseCase =
        RequestBrowsingShapeUseCase(browseLightBulbsRepository)

    @Provides
    fun browsingShapeViewModel(requestBrowsingProductsUseCase: RequestBrowsingShapeUseCase): BrowseShapeViewModel =
        BrowseShapeViewModel(requestBrowsingProductsUseCase, Dispatchers.Main)
}

@Subcomponent(modules = [(BrowseShapeModule::class)])
interface BrowseShapeComponent {
    val browseShapeViewModel: BrowseShapeViewModel
}
