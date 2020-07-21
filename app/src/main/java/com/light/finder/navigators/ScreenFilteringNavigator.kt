package com.light.finder.navigators

import androidx.fragment.app.FragmentManager
import com.light.finder.R
import com.light.finder.ui.browse.BaseFilteringFragment
import com.light.finder.ui.browse.BrowseActivity
import com.light.finder.ui.browse.BrowseFittingFragment


class ScreenFilteringNavigator(private val activity: BrowseActivity) {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToBrowsingFittingScreen() {
       setFragmentTransaction(BrowseFittingFragment())
    }

    fun navigateToBrowsingShapeScreen() {

    }

    private fun setFragmentTransaction(fragmentFiltering: BaseFilteringFragment){
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container_browse, fragmentFiltering).commit()
    }


}