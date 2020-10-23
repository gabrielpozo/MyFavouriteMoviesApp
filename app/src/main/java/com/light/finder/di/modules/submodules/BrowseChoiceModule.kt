package com.light.finder.di.modules.submodules

import com.light.domain.BrowseChoiceRepository
import com.light.domain.ShapeLightBulbRepository
import com.light.presentation.viewmodels.BrowseChoiceViewModel
import com.light.presentation.viewmodels.BrowseShapeViewModel
import com.light.usecases.GetChoiceEditBrowseUseCase
import com.light.usecases.RequestBrowsingChoiceUseCase
import com.light.usecases.RequestBrowsingShapeUseCase
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
    fun getChoiceProductsUseCase(browseLightBulbsRepository: ShapeLightBulbRepository): GetChoiceEditBrowseUseCase =
        GetChoiceEditBrowseUseCase(browseLightBulbsRepository)

    @Provides
    fun browsingChoiceViewModel(
        requestBrowsingProductsUseCase: RequestBrowsingChoiceUseCase,
        getChoiceEditBrowseUseCase: GetChoiceEditBrowseUseCase
    ): BrowseChoiceViewModel =
        BrowseChoiceViewModel(
            requestBrowsingProductsUseCase,
            getChoiceEditBrowseUseCase,
            Dispatchers.Main
        )
}

@Subcomponent(modules = [(BrowseChoiceModule::class)])
interface BrowseChoiceComponent {
    val browseChoiceViewModel: BrowseChoiceViewModel
}
