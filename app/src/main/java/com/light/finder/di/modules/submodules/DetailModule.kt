package com.light.finder.di.modules.submodules


import com.light.domain.CartItemCountRepository
import com.light.domain.CartRepository
import com.light.presentation.viewmodels.DetailViewModel
import com.light.usecases.*
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class DetailModule {

    @Provides
    fun getDetailsUseCase(cartRepository: CartRepository) =
        GetAddToCartUseCase(cartRepository)

    @Provides
    fun getItemCountUseCase(cartItemCountRepository: CartItemCountRepository) =
        GetItemCountUseCase(cartItemCountRepository)

    @Provides
    fun getWattageVariationsUseCase() =
        GetWattageVariationsUseCase()

    @Provides
    fun getColorVariationsUseCase() =
        GetColorVariationsUseCase()

    @Provides
    fun getFinishVariationsUseCase() =
        GetFinishVariationsUseCase()

    @Provides
    fun getAvailableSelectedFilterUseCase() =
        GetNewCompatibleVariationListUseCase()

    @Provides
    fun getNewSelectedProduct() =
        GetNewIncompatibleVariationListUseCase()

    @Provides
    fun getCctDisplayedCctCodesList() =
        GetDisplayedCctCodesUseCase()

    @Provides
    fun categoryViewModel(
        detailsUseCase: GetAddToCartUseCase,
        getItemCountUseCase: GetItemCountUseCase,
        getDisplayedCctCodes: GetDisplayedCctCodesUseCase
    ) = DetailViewModel(
        detailsUseCase,
        getItemCountUseCase,
        getWattageVariationsUseCase(),
        getColorVariationsUseCase(),
        getFinishVariationsUseCase(),
        getAvailableSelectedFilterUseCase(),
        getNewSelectedProduct(),
        getDisplayedCctCodes,
        Dispatchers.Main
    )

}

@Subcomponent(modules = [(DetailModule::class)])
interface DetailComponent {
    val detailViewModel: DetailViewModel
}
