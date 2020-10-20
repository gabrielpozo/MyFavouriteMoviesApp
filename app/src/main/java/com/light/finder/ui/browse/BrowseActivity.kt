package com.light.finder.ui.browse

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.BaseLightFinderActivity
import com.light.finder.R
import com.light.finder.di.modules.filter.BrowseFilteringComponent
import com.light.finder.di.modules.filter.BrowseFilteringModule
import com.light.finder.extensions.app
import com.light.finder.navigators.ScreenFilteringNavigator
import com.light.finder.ui.filter.FilterLightFinderActivity


class BrowseActivity : BaseLightFinderActivity() {

    companion object {
        const val REQUEST_CODE_BROWSING = 2
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

        screenFilteringNavigator.navigateToBrowsingFittingScreen()

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