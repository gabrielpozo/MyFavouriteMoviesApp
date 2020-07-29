package com.light.finder.ui.browse

import android.content.Context
import androidx.fragment.app.Fragment
import com.light.finder.di.modules.filter.BrowseFilteringComponent
import com.light.finder.navigators.ScreenFilteringNavigator


abstract class BaseFilteringFragment : Fragment() {
    protected lateinit var screenFilteringNavigator: ScreenFilteringNavigator
    protected lateinit var browseComponent: BrowseFilteringComponent


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BrowseActivity) {
            browseComponent = context.browsingFilteringComponent
            screenFilteringNavigator = browseComponent.screenFilteringNavigator
        }
    }

}