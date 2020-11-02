package com.light.finder.ui

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.light.finder.CameraLightFinderActivity
import com.light.finder.data.analytics.FacebookAnalyticsUtil
import com.light.finder.di.modules.camera.LightFinderComponent
import com.light.finder.navigators.ScreenNavigator


abstract class BaseFragment : Fragment() {
    protected lateinit var screenNavigator: ScreenNavigator
    protected lateinit var lightFinderComponent: LightFinderComponent
    protected lateinit var firebaseAnalytics: FirebaseAnalytics
    protected lateinit var facebookAnalyticsUtil: FacebookAnalyticsUtil

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CameraLightFinderActivity) {
            lightFinderComponent = context.lightFinderComponent
            screenNavigator = lightFinderComponent.screenNavigator
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            facebookAnalyticsUtil = FacebookAnalyticsUtil.getInstance(context)
        }
    }
}