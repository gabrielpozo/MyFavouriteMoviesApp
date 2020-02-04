package com.accenture.signify.di.modules

import com.accenture.domain.CategoryRepository
import com.accenture.presentation.viewmodels.CategoryViewModel
import com.accenture.usecases.GetCategoriesResultUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class CategoriesModule {

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