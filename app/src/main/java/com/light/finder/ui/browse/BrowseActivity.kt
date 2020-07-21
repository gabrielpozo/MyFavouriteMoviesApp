package com.light.finder.ui.browse

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.BaseLightFinderActivity
import com.light.finder.R
import kotlinx.android.synthetic.main.activity_browse.*

class BrowseActivity : BaseLightFinderActivity(){

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var container: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setContentView(R.layout.activity_browse)
        container = findViewById(R.id.fragment_container_browse)

        textResetSkip.paintFlags = textResetSkip.paintFlags or Paint.UNDERLINE_TEXT_FLAG



    }


}