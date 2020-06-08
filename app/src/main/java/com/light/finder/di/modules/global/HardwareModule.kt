package com.light.finder.di.modules.global

import com.light.finder.common.ShakeDetector
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class HardwareModule(private val listener: ShakeDetector.Listener) {

    @GlobalCameraHardwareScope
    @Provides
    fun getItemCountUseCase() =
        ShakeDetector(listener)
}


@GlobalCameraHardwareScope
@Subcomponent(modules = [(HardwareModule::class)])
interface HardwareInstanceComponents {
    val shakeDetector: ShakeDetector
}
