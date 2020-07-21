package com.light.finder.ui.browse


import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.domain.model.ProductBrowsing
import com.light.finder.R
import com.light.finder.di.modules.submodules.BrowseFittingModule
import com.light.finder.di.modules.submodules.BrowsingFittingComponent
import com.light.finder.extensions.getViewModel
import com.light.finder.extensions.gone
import com.light.finder.extensions.visible
import com.light.finder.ui.adapters.BrowseFittingAdapter
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.BrowseFittingViewModel
import com.light.presentation.viewmodels.BrowseFittingViewModel.UiBrowsingModel
import kotlinx.android.synthetic.main.activity_browse.*
import kotlinx.android.synthetic.main.fragment_browse_fitting.*
import kotlinx.android.synthetic.main.layout_browse_loading.*

class BrowseFittingFragment : BaseFilteringFragment() {

    private lateinit var component: BrowsingFittingComponent
    private lateinit var adapter: BrowseFittingAdapter

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
        setFittingListeners()
    }

    private fun setAdapter() {
        /*adapter = BrowseFittingAdapter(
            viewModel::onFittingClick,
            //todo pass browsing product list
        )
        recyclerViewFitting.adapter = adapter*/
    }

    private fun setObservers() {
        viewModel.modelBrowsingLiveData.observe(
            viewLifecycleOwner,
            Observer(::updateBrowsingFittingUI)
        )
        viewModel.modelNavigationShape.observe(viewLifecycleOwner, Observer(::navigatesToShape))
    }

    private fun setFittingListeners() {
        buttonSearch.setOnClickListener {
            viewModel.onSearchButtonPressed()
        }
    }

    private fun updateBrowsingFittingUI(modelBrowse: UiBrowsingModel) {
        when (modelBrowse) {
            is UiBrowsingModel.SuccessRequestStatus -> {
                showFittings(modelBrowse.productBrowsingList)
            }
            is UiBrowsingModel.ErrorRequestStatus -> {
                showError()
            }

            is UiBrowsingModel.LoadingStatus -> {
                showLoading()
            }
        }
    }

    private fun navigatesToShape(modelNavigation: Event<BrowseFittingViewModel.NavigationToShapeFiltering>) {
        modelNavigation.getContentIfNotHandled()?.let {
            screenFilteringNavigator.navigateToBrowsingShapeScreen()
        }
    }

    private fun showLoading() {
        recyclerViewFitting.gone()
        browseError.gone()
        browseLoading.visible()
        with(browseLoadingAnimation) {
            playAnimation()
            repeatCount = ValueAnimator.INFINITE
        }
    }

    private fun showError() {
        recyclerViewFitting.gone()
        browseError.visible()
        browseLoading.gone()
    }

    private fun showFittings(productBrowsingList: List<ProductBrowsing>) {
        recyclerViewFitting.visible()
        browseError.gone()
        browseLoading.gone()
        //TODO add the list here to the adapter (productBrowsingList)
    }
}