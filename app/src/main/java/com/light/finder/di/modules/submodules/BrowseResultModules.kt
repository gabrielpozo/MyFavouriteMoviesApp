package com.light.finder.di.modules.submodules

import com.light.domain.ProductBrowsingRepository
import com.light.domain.ShapeLightBulbRepository
import com.light.presentation.viewmodels.BrowseResultViewModel
import com.light.usecases.GetChoiceBrowsingProductsUseCase
import com.light.usecases.GetChoiceEditBrowseUseCase
import com.light.usecases.GetShapeEditBrowseUseCase
import com.light.usecases.GetSortCategoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class BrowseResultModule {

    @Provides
    fun getSortingUseCase() = GetSortCategoriesUseCase()

    @Provides
    fun getResultUseCase(productBrowsingRepository: ProductBrowsingRepository) =
        GetChoiceBrowsingProductsUseCase(productBrowsingRepository)


    @Provides
    fun getChoiceProductsUseCase(browseLightBulbsRepository: ShapeLightBulbRepository): GetChoiceEditBrowseUseCase =
        GetChoiceEditBrowseUseCase(browseLightBulbsRepository)

    @Provides
    fun getShapeProductsUseCase(browseLightBulbsRepository: ShapeLightBulbRepository): GetShapeEditBrowseUseCase =
        GetShapeEditBrowseUseCase(browseLightBulbsRepository)

    @Provides
    fun browseResultViewModel(
        getSortCategoriesUseCase: GetSortCategoriesUseCase,
        getBrowseProductsResultUseCase: GetChoiceBrowsingProductsUseCase,
        getShapeBrowsingListUseCase: GetChoiceEditBrowseUseCase,
        getShapeEditBrowseUseCase: GetShapeEditBrowseUseCase
    ) = BrowseResultViewModel(
        getSortCategoriesUseCase,
        getBrowseProductsResultUseCase,
        getShapeBrowsingListUseCase,
        getShapeEditBrowseUseCase,
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(BrowseResultModule::class)])
interface BrowseResultComponent {
    val browseResultViewModel: BrowseResultViewModel
}