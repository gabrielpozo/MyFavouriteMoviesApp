package com.light.finder.navigators

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.light.finder.R
import com.light.finder.ui.browse.BaseFilteringFragment
import com.light.finder.ui.browse.BrowseActivity
import com.light.finder.ui.browse.BrowseFittingFragment
import com.light.finder.ui.browse.BrowseShapeFragment


class ScreenFilteringNavigator(private val activity: BrowseActivity) {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToBrowsingFittingScreen() {
        setFragmentTransaction(BrowseFittingFragment())
    }

    fun navigateToBrowsingShapeScreen(productBaseId: Int) {
        val bundle = Bundle()
        bundle.putInt(BrowseShapeFragment.SHAPE_ID_KEY, productBaseId)
        val browseShapeFragment = BrowseShapeFragment()
        browseShapeFragment.arguments = bundle
        setFragmentTransaction(browseShapeFragment)
    }

    private fun setFragmentTransaction(fragmentFiltering: BaseFilteringFragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container_browse, fragmentFiltering).commit()
    }


}