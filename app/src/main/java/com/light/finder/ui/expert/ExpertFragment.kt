package com.light.finder.ui.expert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.light.finder.R
import com.light.finder.ui.BaseFragment

class ExpertFragment : BaseFragment(){
    companion object;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.empty_fragment, container, false)
    }
}