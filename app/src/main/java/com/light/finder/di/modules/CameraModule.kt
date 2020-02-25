package com.light.finder.di.modules

import com.light.domain.CategoryRepository
import com.light.presentation.viewmodels.CameraViewModel
import com.light.usecases.GetCategoriesResultUseCase
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
    fun cameraViewModel(
        getCategoryResultUseCase: GetCategoriesResultUseCase

    ) = CameraViewModel(
        getCategoryResultUseCase, Dispatchers.Main
    )
}

@Subcomponent(modules = [(CameraModule::class)])
interface CameraComponent {
    val cameraViewModel: CameraViewModel
}