package com.light.finder.ui.browse

import android.content.Context
import androidx.fragment.app.Fragment
import com.light.finder.di.modules.filter.BrowseFilteringComponent


abstract class BaseFilteringFragment : Fragment() {
   // protected lateinit var screenNavigator: ScreenNavigator
    protected lateinit var browseComponent: BrowseFilteringComponent


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BrowseActivity) {
            browseComponent = context.browsingFilteringComponent
          //  screenNavigator = lightFinderComponent.screenNavigator
        }
    }

}