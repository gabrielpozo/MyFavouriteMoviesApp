package com.light.finder.ui.browse

import android.os.Bundle


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.finder.R
import com.light.finder.di.modules.submodules.BrowseFittingModule
import com.light.finder.di.modules.submodules.BrowsingFittingComponent
import com.light.finder.extensions.getViewModel
import com.light.presentation.viewmodels.BrowseFittingViewModel
import com.light.presentation.viewmodels.BrowseFittingViewModel.*

class BrowseFittingFragment : BaseFilteringFragment() {

    private lateinit var component: BrowsingFittingComponent

    private val viewModel: BrowseFittingViewModel by lazy { getViewModel { component.browseFittingViewModel } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_browse_fitting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component = browseComponent.plus(BrowseFittingModule())
        }

        setAdapter()
        setObservers()
    }

    private fun setAdapter() {
        //TODO("initialize the adapter here")
    }

    private fun setObservers() {
        viewModel.modelBrowsingLiveData.observe(
            viewLifecycleOwner,
            Observer(::updateBrowsingFittingUI)
        )
    }

    private fun updateBrowsingFittingUI(modelBrowse: UiBrowsingModel) {
        when (modelBrowse) {
            is UiBrowsingModel.SuccessRequestStatus -> {
                //TODO("Not yet implemented")

            }
            is UiBrowsingModel.ErrorRequestStatus -> {
                //TODO("Not yet implemented")

            }

            is UiBrowsingModel.LoadingStatus -> {
                //TODO("Not yet implemented")

            }
        }
    }
}