package com.light.finder.navigators

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.light.domain.model.ShapeBrowsing
import com.light.finder.R
import com.light.finder.ui.browse.BaseFilteringFragment
import com.light.finder.ui.browse.BrowseActivity
import com.light.finder.ui.browse.BrowseFittingFragment
import com.light.finder.ui.browse.BrowseShapeFragment


class ScreenFilteringNavigator(activity: BrowseActivity) {

    companion object {
        private const val FILTERING_BACKSTAGE = "filtering_backstage"
    }

    val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToBrowsingFittingScreen() {
        addFragmentTransaction(BrowseFittingFragment())
    }

    fun navigateToBrowsingShapeScreen(productBaseId: Int) {
        val bundle = Bundle()
        bundle.putInt(BrowseShapeFragment.SHAPE_ID_KEY, productBaseId)
        val browseShapeFragment = BrowseShapeFragment()
        browseShapeFragment.arguments = bundle
        replaceFragmentTransaction(browseShapeFragment)
    }

    private fun addFragmentTransaction(fragmentFiltering: BaseFilteringFragment) {
        fragmentManager.beginTransaction().setCustomAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_to_left,
            R.anim.slide_in_from_left,
            R.anim.slide_out_to_right
        ).add(R.id.fragment_container_browse, fragmentFiltering)
            .commit()
    }

    private fun replaceFragmentTransaction(fragmentFiltering: BaseFilteringFragment) {
        fragmentManager.beginTransaction().setCustomAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_to_left,
            R.anim.slide_in_from_left,
            R.anim.slide_out_to_right
        )
            .addToBackStack(null)
            .replace(R.id.fragment_container_browse, fragmentFiltering)
            .commit()
    }


    fun popFragment() {
        fragmentManager.popBackStack()
    }

    fun navigateToResultCategories(productsShapeSelected: List<ShapeBrowsing>) {
        //TODO()
    }


}