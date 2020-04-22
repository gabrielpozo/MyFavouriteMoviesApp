package com.light.finder.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.light.finder.CameraActivity
import com.light.finder.common.ScreenNavigator
import com.light.finder.di.modules.LightFinderComponent


abstract class BaseFragment : Fragment() {
    protected lateinit var screenNavigator: ScreenNavigator
    protected lateinit var lightFinderComponent: LightFinderComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CameraActivity) {
            screenNavigator = context.lightFinderComponent.screenNavigator
            lightFinderComponent = context.lightFinderComponent
        }
    }

}