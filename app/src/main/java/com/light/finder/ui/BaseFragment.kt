package com.light.finder.ui

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {

    lateinit var mFragmentNavigation: FragmentNavigation

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentNavigation) {
            mFragmentNavigation = context
        }
    }


    interface FragmentNavigation {
        fun pushFragment(fragment: Fragment, sharedElementList: List<Pair<View, String>>? = null)
        fun popFragment()
    }
}