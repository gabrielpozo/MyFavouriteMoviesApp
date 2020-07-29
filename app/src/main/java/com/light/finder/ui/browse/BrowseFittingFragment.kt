package com.light.finder.ui.browse


import android.animation.ValueAnimator
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.paris.extensions.backgroundRes
import com.airbnb.paris.extensions.style
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.light.domain.model.FormFactorTypeBaseId
import com.light.finder.R
import com.light.finder.di.modules.submodules.BrowseFittingModule
import com.light.finder.di.modules.submodules.BrowsingFittingComponent
import com.light.finder.extensions.getViewModel
import com.light.finder.extensions.gone
import com.light.finder.extensions.trackScreen
import com.light.finder.extensions.visible
import com.light.finder.ui.adapters.BrowseFittingAdapter
import com.light.finder.ui.itemdecoration.FittingItemDecoration
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.BrowseFittingViewModel
import com.light.presentation.viewmodels.BrowseFittingViewModel.UiBrowsingModel
import kotlinx.android.synthetic.main.fragment_browse_fitting.*
import kotlinx.android.synthetic.main.layout_browse_error.*
import kotlinx.android.synthetic.main.layout_browse_loading.*


class BrowseFittingFragment : BaseFilteringFragment() {

    private lateinit var component: BrowsingFittingComponent
    private lateinit var adapter: BrowseFittingAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val BROWSE_SCREEN_TAG ="BrowseChooseFitting"
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
            firebaseAnalytics.trackScreen(this@BrowseFittingFragment, this, BROWSE_SCREEN_TAG)
        }

        buttonNext.setOnClickListener {
            viewModel.onNextButtonPressed()
        }



        buttonRefresh.setOnClickListener {
            viewModel.onRequestBrowsingProducts()
        }

        setAdapter()
        setBottomSheetBehaviour()
        setObservers()
    }


    private fun setBottomSheetBehaviour() {
        val bottomSheetLayout = view?.findViewById<LinearLayout>(R.id.bottomSheetLayoutBrowse)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)


        context?.let {
            val displayMetrics = it.resources.displayMetrics
            val dpHeight = displayMetrics.heightPixels

            bottomSheetBehavior.peekHeight = (dpHeight / 1.5).toInt()

        }
    }

    private fun setAdapter() {
        adapter = BrowseFittingAdapter(
            viewModel::onFittingClick
        )
        adapter.setHasStableIds(true)
        val layoutManager = GridLayoutManager(context, 3)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        recyclerViewFitting.addItemDecoration(FittingItemDecoration(context!!, R.dimen.spacing))
        recyclerViewFitting.layoutManager = layoutManager
        recyclerViewFitting.adapter = adapter
    }


    private fun setObservers() {
        viewModel.modelBrowsingLiveData.observe(
            viewLifecycleOwner,
            Observer(::updateBrowsingFittingUI)
        )
        viewModel.modelNavigationShape.observe(viewLifecycleOwner, Observer(::navigatesToShape))
        viewModel.modelBottomStatus.observe(viewLifecycleOwner, Observer(::updateStatusBottomBar))
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

    private fun updateStatusBottomBar(modelEvent: Event<BrowseFittingViewModel.StatusBottomBar>) {
        modelEvent.getContentIfNotHandled()?.let { model ->
            when (model) {
                is BrowseFittingViewModel.StatusBottomBar.ResetFitting -> {
                    resetFittingSelection()
                }
                is BrowseFittingViewModel.StatusBottomBar.FittingClicked -> {
                    settingFilterSelected()
                }
            }
        }
    }

    private fun navigatesToShape(modelNavigation: Event<BrowseFittingViewModel.NavigationToShapeFiltering>) {
        modelNavigation.getContentIfNotHandled()?.let { model ->
            screenFilteringNavigator.navigateToBrowsingShapeScreen(model.productBaseId)
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

    private fun showFittings(productFittingList: List<FormFactorTypeBaseId>) {
        recyclerViewFitting.visible()
        browseError.gone()
        browseLoading.gone()
        adapter.setFittingProductList(productFittingList)
    }

    private fun resetFittingSelection() {
        buttonNext.style {
            add(R.style.BrowseNextDisable)
            backgroundRes(R.drawable.browse_next_disable)
        }
        adapter.clearSelection()
    }

    private fun settingFilterSelected() {
        buttonNext.style {
            add(R.style.TitleTextGray)
            backgroundRes(R.drawable.button_curvy_corners)
        }
    }
}