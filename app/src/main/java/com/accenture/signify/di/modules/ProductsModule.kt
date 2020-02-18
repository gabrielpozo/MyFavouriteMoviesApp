package com.accenture.signify.di.modules

import com.accenture.presentation.viewmodels.ProductsViewModel
import com.accenture.usecases.GetFilterButtonsUseCase
import com.accenture.usecases.GetProductsFilteredUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class ProductsModule {

    @Provides
    fun getProductsFilteredUseCase() =
        GetProductsFilteredUseCase()

    @Provides
    fun getFilterListUseCase() =
        GetFilterButtonsUseCase()

    @Provides
    fun categoryViewModel(
        productsFilteredUseCase: GetProductsFilteredUseCase,
        filterButtonsUseCase: GetFilterButtonsUseCase
    ) = ProductsViewModel(
        productsFilteredUseCase,
        filterButtonsUseCase,
        Dispatchers.Main
    )

}

@Subcomponent(modules = [(ProductsModule::class)])
interface ProductsComponent {
    val productsViewModel: ProductsViewModel
}

