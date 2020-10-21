package com.light.finder.navigators

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.model.ShapeBrowsing
import com.light.finder.CameraLightFinderActivity
import com.light.finder.CameraLightFinderActivity.Companion.BROWSING_ACTIVITY
import com.light.finder.CameraLightFinderActivity.Companion.CAMERA_LIGHT_FINDER_ACTIVITY_ID
import com.light.finder.R
import com.light.finder.extensions.*
import com.light.finder.ui.browse.*
import com.ncapdevi.fragnav.FragNavController


class ScreenFilteringNavigator(private val activity: BrowseActivity) {

    companion object {
        private const val FILTERING_BACKSTAGE = "filtering_backstage"
    }

    val fragmentManager: FragmentManager = activity.supportFragmentManager
    private val fragNavController: FragNavController =
        FragNavController(activity.supportFragmentManager, R.id.fragment_container_browse)

    fun navigateToBrowsingFittingScreen() {
        addFragmentTransaction(BrowseFittingFragment())
    }

    fun navigateToBrowsingShapeScreen(productBaseId: FormFactorTypeBaseId) {
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

    fun navigateToResultCategories(productsChoiceSelected: List<ChoiceBrowsing>) {
        activity.startActivity<CameraLightFinderActivity> {
            putExtra(CAMERA_LIGHT_FINDER_ACTIVITY_ID, BROWSING_ACTIVITY)
            putParcelableArrayListExtra(
                CameraLightFinderActivity.BROWSING_SHAPE_VALUES_ID,
                productsChoiceSelected.parcelizeChoiceBrowsingList()
            )
        }
        activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }


    fun navigateToBrowsingChoiceScreen(productsShapeSelected: List<ShapeBrowsing>) {
        replaceFragmentTransaction(BrowseChoiceFragment.newInstance(productsShapeSelected))
    }

    fun navigateFirstTimeToBrowsingChoiceScreen(productsChoiceSelected: List<ChoiceBrowsing>) {
        addFragmentTransaction(BrowseChoiceFragment.newInstanceForEditBrowse(productsChoiceSelected))
    }

    fun navigateFirstTimeToBrowsingShapeScreen(shapeParcelable: List<ShapeBrowsing>) {
        addFragmentTransaction(BrowseShapeFragment.newInstanceForEditBrowse(shapeParcelable))
    }

    fun navigateEditBrowsingFittingScreen() {
        addFragmentTransaction(BrowseFittingFragment.newInstanceForFittingEditBrowse())
    }

    fun popFragment() {
        fragmentManager.popBackStack()
    }

    fun getCurrentFragment(): Fragment? = fragNavController.currentFrag


}