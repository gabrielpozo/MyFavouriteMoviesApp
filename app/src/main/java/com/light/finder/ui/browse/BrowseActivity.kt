package com.light.finder.ui.browse

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.BaseLightFinderActivity
import com.light.finder.R
import com.light.finder.data.source.remote.ChoiceBrowsingParcelable
import com.light.finder.data.source.remote.ShapeBrowsingParcelable
import com.light.finder.di.modules.filter.BrowseFilteringComponent
import com.light.finder.di.modules.filter.BrowseFilteringModule
import com.light.finder.extensions.app
import com.light.finder.extensions.deParcelizeBrowsingList
import com.light.finder.extensions.deparcelizeChoiceBrowsingList
import com.light.finder.navigators.ScreenFilteringNavigator
import com.light.finder.ui.filter.FilterLightFinderActivity


class BrowseActivity : BaseLightFinderActivity() {

    companion object {
        const val REQUEST_CODE_BROWSING = 0
        const val REQUEST_BROWSING_SCREEN = "REQUEST_BROWSING_SCREEN"
        const val FITTING_VIEW = 2
        const val SHAPE_VIEW = 3
        const val CATEGORY_CHOICE_VIEW = 4
        const val CHOICE_LIST_CODE = "CHOICE_LIST_CODE"
        const val SHAPE_LIST_CODE = "SHAPE_LIST_CODE"


    }

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val screenFilteringNavigator: ScreenFilteringNavigator by lazy { browsingFilteringComponent.screenFilteringNavigator }
    val browsingFilteringComponent: BrowseFilteringComponent by lazy {
        app.applicationComponent.plus(
            BrowseFilteringModule(
                this
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setContentView(R.layout.activity_browse)

        intent.getIntExtra(REQUEST_BROWSING_SCREEN, -1).let { requestCodeScreen ->
            when (requestCodeScreen) {
                FITTING_VIEW -> {
                    screenFilteringNavigator.navigateEditBrowsingFittingScreen()
                }
                SHAPE_VIEW -> {
                    val shapeParcelable =
                        intent.getParcelableArrayListExtra<ShapeBrowsingParcelable>(
                            SHAPE_LIST_CODE
                        )
                    screenFilteringNavigator.navigateFirstTimeToBrowsingShapeScreen(shapeParcelable.deParcelizeBrowsingList())
                }
                CATEGORY_CHOICE_VIEW -> {
                    val choiceParcelable =
                        intent.getParcelableArrayListExtra<ChoiceBrowsingParcelable>(
                            CHOICE_LIST_CODE
                        )
                    screenFilteringNavigator.navigateFirstTimeToBrowsingChoiceScreen(
                        choiceParcelable.deparcelizeChoiceBrowsingList()
                    )
                }

                else -> {
                    screenFilteringNavigator.navigateToBrowsingFittingScreen()
                }


            }
        }


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        //TODO perform the new screen here

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_BROWSING) {
            val currentFragment = screenFilteringNavigator.getCurrentFragment()
            if (currentFragment is BrowseResultFragment) {
                data?.getIntExtra(
                    FilterLightFinderActivity.SORT_ID,
                    2131362146
                ) ?: -1
            }
        }
    }

    override fun onBackPressed() {
        if (screenFilteringNavigator.fragmentManager.backStackEntryCount > 0) {
            screenFilteringNavigator.popFragment()
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)

        }
    }
}