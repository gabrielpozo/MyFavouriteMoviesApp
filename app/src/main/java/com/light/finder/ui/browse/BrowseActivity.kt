package com.light.finder.ui.browse

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.BaseLightFinderActivity
import com.light.finder.R
import com.light.finder.di.modules.filter.BrowseFilteringComponent
import com.light.finder.di.modules.filter.BrowseFilteringModule
import com.light.finder.extensions.app
import com.light.finder.navigators.ScreenFilteringNavigator
import kotlinx.android.synthetic.main.activity_browse.*


class BrowseActivity : BaseLightFinderActivity(){

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var container: FrameLayout
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
        container = findViewById(R.id.fragment_container_browse)

        textResetSkip.paintFlags = textResetSkip.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        screenFilteringNavigator.navigateToBrowsingFittingScreen()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }


}