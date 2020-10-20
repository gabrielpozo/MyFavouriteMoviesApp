package com.light.finder.di.modules.filter

import com.light.finder.di.modules.submodules.*
import com.light.finder.navigators.ScreenFilteringNavigator
import com.light.finder.ui.browse.BrowseActivity
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class BrowseFilteringModule(private val context: BrowseActivity) {

    @FilterScope
    @Provides
    fun getScreenFilteringNavigator() =
        ScreenFilteringNavigator(context)
}

@FilterScope
@Subcomponent(modules = [(BrowseFilteringModule::class)])
interface BrowseFilteringComponent {
    val screenFilteringNavigator: ScreenFilteringNavigator
    fun plus(module: BrowseFittingModule): BrowsingFittingComponent
    fun plus(module: BrowseShapeModule): BrowseShapeComponent
    fun plus(module: BrowseChoiceModule): BrowseChoiceComponent
}