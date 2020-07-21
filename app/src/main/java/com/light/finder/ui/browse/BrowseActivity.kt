package com.light.finder.ui.browse

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.BaseLightFinderActivity
import com.light.finder.R
import com.light.finder.di.modules.camera.LightFinderComponent
import com.light.finder.di.modules.camera.LightFinderModule
import com.light.finder.di.modules.filter.BrowseFilteringComponent
import com.light.finder.di.modules.filter.BrowseFilteringModule
import com.light.finder.extensions.app

class BrowseActivity : BaseLightFinderActivity(){

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var container: FrameLayout

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
    }


}