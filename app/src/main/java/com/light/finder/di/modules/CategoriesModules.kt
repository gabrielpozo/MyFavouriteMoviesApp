package com.light.finder.di.modules

import com.light.domain.CategoryRepository
import com.light.presentation.viewmodels.CategoryViewModel
import com.light.usecases.GetCategoriesResultUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class CategoriesModule() {

    @Provides
    fun getCategoriesResultUseCase(categoryRepository: CategoryRepository) =
        GetCategoriesResultUseCase(categoryRepository)

    @Provides
    fun categoryViewModel(
        getCategoryResultUseCase: GetCategoriesResultUseCase

    ) = CategoryViewModel(
        getCategoryResultUseCase,
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(CategoriesModule::class)])
interface CategoriesComponent {
    val categoryViewModel: CategoryViewModel
}