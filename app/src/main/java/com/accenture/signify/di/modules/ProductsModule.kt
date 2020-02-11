package com.accenture.signify.di.modules

import com.accenture.domain.CategoryRepository
import com.accenture.presentation.viewmodels.ProductsViewModel
import com.accenture.usecases.GetCategoriesResultUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class ProductsModule {

    @Provides
    fun getCategoriesResultUseCase(categoryRepository: CategoryRepository) =
        GetCategoriesResultUseCase(categoryRepository)

    @Provides
    fun categoryViewModel(
        getCategoryResultUseCase: GetCategoriesResultUseCase
    ) = ProductsViewModel(
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(ProductsModule::class)])
interface ProductsComponent {
    val productsViewModel: ProductsViewModel
}

