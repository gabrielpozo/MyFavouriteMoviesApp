package com.light.finder.di.modules.submodules

import com.light.domain.CartItemCountRepository
import com.light.domain.CategoryRepository
import com.light.domain.LegendRepository
import com.light.finder.data.source.local.ImageRepository
import com.light.presentation.viewmodels.CameraViewModel
import com.light.source.local.LocalPreferenceDataSource
import com.light.usecases.GetCategoriesResultUseCase
import com.light.usecases.GetFilePathImageEncodedUseCase
import com.light.usecases.GetItemCountUseCase
import com.light.usecases.GetLegendUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers


@Module
class CameraModule {

    @Provides
    fun getCategoriesResultUseCase(categoryRepository: CategoryRepository) =
        GetCategoriesResultUseCase(categoryRepository)

    @Provides
    fun getFilePathImageEncodedUseCase(categoryRepository: CategoryRepository) =
        GetFilePathImageEncodedUseCase(categoryRepository)

    @Provides
    fun getItemCountUseCase(cartItemCountRepository: CartItemCountRepository) =
        GetItemCountUseCase(cartItemCountRepository)

    @Provides
    fun getLegendUseCase(legendRepository: LegendRepository) =
        GetLegendUseCase(legendRepository)


    @Provides
    fun getImageRepository() =
        ImageRepository(Dispatchers.Main)

    @Provides
    fun cameraViewModel(
        getItemCountUseCase: GetItemCountUseCase,
        getCategoryResultUseCase: GetCategoriesResultUseCase
    ) = CameraViewModel(
        getItemCountUseCase,
        getCategoryResultUseCase,
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(CameraModule::class)])
interface CameraComponent {
    val cameraViewModel: CameraViewModel
    val imageRepository: ImageRepository
}