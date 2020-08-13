package com.light.finder.navigators

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.model.ShapeBrowsing
import com.light.finder.CameraLightFinderActivity
import com.light.finder.CameraLightFinderActivity.Companion.BROWSING_ACTIVITY
import com.light.finder.CameraLightFinderActivity.Companion.CAMERA_LIGHT_FINDER_ACTIVITY_ID
import com.light.finder.R
import com.light.finder.extensions.newInstance
import com.light.finder.extensions.parcelizeBrowsingList
import com.light.finder.extensions.startActivity
import com.light.finder.ui.browse.BaseFilteringFragment
import com.light.finder.ui.browse.BrowseActivity
import com.light.finder.ui.browse.BrowseFittingFragment
import com.light.finder.ui.browse.BrowseShapeFragment


class ScreenFilteringNavigator(private val activity: BrowseActivity) {

    companion object {
        private const val FILTERING_BACKSTAGE = "filtering_backstage"
    }

    val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToBrowsingFittingScreen() {
        addFragmentTransaction(BrowseFittingFragment())
    }

    fun navigateToBrowsingShapeScreen(productBaseId: FormFactorTypeBaseId) {

/*        val bundle = Bundle()
        bundle.putInt(BrowseShapeFragment.SHAPE_ID_KEY, productBaseId)
        val browseShapeFragment = BrowseShapeFragment()
        browseShapeFragment.arguments = bundle*/
        replaceFragmentTransaction(BrowseShapeFragment.newInstance(productBaseId))
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
        activity.startActivity<CameraLightFinderActivity> {
            putExtra(CAMERA_LIGHT_FINDER_ACTIVITY_ID, BROWSING_ACTIVITY)
            putParcelableArrayListExtra(
                CameraLightFinderActivity.BROWSING_SHAPE_VALUES_ID,
                productsShapeSelected.parcelizeBrowsingList()
            )
        }
        activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }


}