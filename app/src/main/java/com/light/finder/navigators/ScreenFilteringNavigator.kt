package com.light.finder.navigators

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.light.domain.model.ChoiceBrowsing
import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.model.ShapeBrowsing
import com.light.finder.CameraLightFinderActivity
import com.light.finder.CameraLightFinderActivity.Companion.BROWSING_ACTIVITY
import com.light.finder.CameraLightFinderActivity.Companion.BROWSING_FORM_FACTOR_VALUE_ID
import com.light.finder.CameraLightFinderActivity.Companion.BROWSING_FORM_FACTOR_VALUE_NAME
import com.light.finder.CameraLightFinderActivity.Companion.CAMERA_LIGHT_FINDER_ACTIVITY_ID
import com.light.finder.R
import com.light.finder.extensions.*
import com.light.finder.ui.browse.*
import com.ncapdevi.fragnav.FragNavController


class ScreenFilteringNavigator(private val activity: BrowseActivity) {

    companion object {
        private const val FILTERING_BACKSTAGE = "filtering_backstage"
        private const val FITTING_TAG = "FITTING_TAG"
        private const val CHOICE_TAG = "CHOICE"
        private const val SHAPE_TAG = "SHAPE_TAG"
    }

    val fragmentManager: FragmentManager = activity.supportFragmentManager
    private val fragNavController: FragNavController =
        FragNavController(activity.supportFragmentManager, R.id.fragment_container_browse)

    fun navigateToBrowsingFittingScreen() {
        addFragmentTransaction(BrowseFittingFragment(), FITTING_TAG)
    }

    fun navigateToBrowsingShapeScreen(
        fittingFragment: BrowseFittingFragment,
        productBaseId: FormFactorTypeBaseId
    ) {
        replaceFragmentTransaction(
            BrowseShapeFragment.newInstance(fittingFragment, productBaseId),
            SHAPE_TAG
        )
    }

    fun navigateToBrowsingChoiceScreen(
        browseShapeFragment: BrowseShapeFragment,
        productsShapeSelected: List<ShapeBrowsing>,
        formFactorTypeBaseId: Int,
        formFactorName: String?
    ) {
        replaceFragmentTransaction(
            BrowseChoiceFragment.newInstance(
                browseShapeFragment,
                productsShapeSelected,
                formFactorTypeBaseId,
                formFactorName
            ), CHOICE_TAG
        )
    }

    private fun addFragmentTransaction(
        fragmentFiltering: BaseFilteringFragment,
        tagFragment: String? = null
    ) {
        fragmentManager.beginTransaction().setCustomAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_to_left,
            R.anim.slide_in_from_left,
            R.anim.slide_out_to_right
        ).add(R.id.fragment_container_browse, fragmentFiltering, tagFragment)
            .commit()
    }

    private fun replaceFragmentTransaction(
        fragmentFiltering: BaseFilteringFragment,
        tagFragment: String = ""
    ) {
        fragmentManager.beginTransaction().setCustomAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_to_left,
            R.anim.slide_in_from_left,
            R.anim.slide_out_to_right
        )
            .addToBackStack(null)
            .replace(R.id.fragment_container_browse, fragmentFiltering, tagFragment)
            .commit()
    }

    fun navigateToResultCategories(
        productsChoiceSelected: List<ChoiceBrowsing>,
        productsShapeSelected: List<ShapeBrowsing>?,
        formFactorId: Int,
        formFactorName: String?
    ) {
        activity.startActivity<CameraLightFinderActivity> {
            if (activity.stateInitiated) {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            putExtra(CAMERA_LIGHT_FINDER_ACTIVITY_ID, BROWSING_ACTIVITY)
            putParcelableArrayListExtra(
                CameraLightFinderActivity.BROWSING_CHOICE_LIST_VALUES_ID,
                productsChoiceSelected.parcelizeChoiceBrowsingList()
            )
            putParcelableArrayListExtra(
                CameraLightFinderActivity.BROWSING_SHAPE_LIST_VALUES_ID,
                productsShapeSelected?.parcelizeBrowsingList()
            )
            putExtra(BROWSING_FORM_FACTOR_VALUE_ID, formFactorId)
            putExtra(BROWSING_FORM_FACTOR_VALUE_NAME, formFactorName)
        }
        activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }


    fun navigateFirstTimeToBrowsingChoiceScreen() {
        addFragmentTransaction(BrowseChoiceFragment.newInstanceForShapeChoiceEditBrowse())
    }

    fun navigateFirstTimeToBrowsingShapeScreen() {
        addFragmentTransaction(BrowseShapeFragment.newInstanceForShapeChoiceEditBrowse())
    }

    fun navigateEditBrowsingFittingScreen() {
        addFragmentTransaction(BrowseFittingFragment.newInstanceForFittingEditBrowse())
    }

    fun popFragment() {
        fragmentManager.popBackStack()
    }


    fun getCurrentFragment(): Fragment? = fragNavController.currentFrag

    fun getCurrentFragment2(): Fragment? =
        fragmentManager.findFragmentById(R.id.fragment_container_browse)


    fun setAllFilteringScreens() {
        val choiceFragment = fragmentManager.findFragmentByTag(CHOICE_TAG)
        val shapeFragment = fragmentManager.findFragmentByTag(SHAPE_TAG)
        val fittingFragment = fragmentManager.findFragmentByTag(FITTING_TAG)
        if (choiceFragment is BrowseExpandableStatus) {
            choiceFragment.setExpandableChoiceSelection()
        }
        if (fittingFragment is BrowseFittingFragment) {
            fittingFragment.setBackFilteringOnScreen(true)
        }

        if (shapeFragment is BrowseShapeFragment) {
            shapeFragment.setBackFilteringOnScreen(true)
        }
    }
}

