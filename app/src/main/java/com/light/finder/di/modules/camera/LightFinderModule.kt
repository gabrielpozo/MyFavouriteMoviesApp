package com.light.finder.di.modules.camera

import com.light.finder.CameraLightFinderActivity
import com.light.finder.common.ScreenNavigator
import com.light.finder.di.modules.submodules.*
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class LightFinderModule(private val context: CameraLightFinderActivity) {

    @CameraScope
    @Provides
    fun getScreenNavigator() =
        ScreenNavigator(context)
}

@CameraScope
@Subcomponent(modules = [(LightFinderModule::class)])
interface LightFinderComponent {
    val screenNavigator: ScreenNavigator

    fun plus(module: CategoriesModule): CategoriesComponent
    fun plus(module: CameraModule): CameraComponent
    fun plus(module: DetailModule): DetailComponent
    fun plus(module: CartModule): CartComponent
    fun plus(module: AboutModule) : AboutComponent
}