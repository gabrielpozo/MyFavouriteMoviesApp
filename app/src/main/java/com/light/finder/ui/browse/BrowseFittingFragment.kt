package com.light.finder.ui.browse


import android.animation.ValueAnimator
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
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
    private var isNextDisabled = true

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

        textResetSkip.paintFlags = textResetSkip.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textResetSkip.setOnClickListener {
            viewModel.onResetButtonPressed()
        }


        buttonNext.setOnClickListener {
            if (!isNextDisabled) {
                viewModel.onNextButtonPressed()
            }
        }



        buttonRefresh.setOnClickListener {
            viewModel.onRequestBrowsingProducts()
        }

        setAdapter()
        setBottomSheetBehaviour()
        setObservers()
    }


    private fun setBottomSheetBehaviour() {
        val bottomSheetLayout = view?.findViewById<NestedScrollView>(R.id.bottomSheetLayoutBrowse)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        // avoid unwanted scroll when bottom sheet collapsed
        ViewCompat.setNestedScrollingEnabled(recyclerViewFitting, false)

        context?.let {
            val displayMetrics = it.resources.displayMetrics
            val dpHeight = displayMetrics.heightPixels
            fittingLayout.updateLayoutParams<ViewGroup.LayoutParams> {
                height = dpHeight
            }
            bottomSheetBehavior.peekHeight = (dpHeight / 1.5).toInt()

        }
    }

    private fun setAdapter() {
        adapter = BrowseFittingAdapter(
            viewModel::onFittingClick,
            ::onClick
        )
        adapter.setHasStableIds(true)
        val layoutManager = GridLayoutManager(context, 3)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        recyclerViewFitting.addItemDecoration(FittingItemDecoration(context!!, R.dimen.spacing))
        recyclerViewFitting.layoutManager = layoutManager
        recyclerViewFitting.adapter = adapter
    }

    private fun onClick(isClicked: Boolean) {
        if (isClicked)
            textResetSkip.visible()
        buttonNext.style {
            add(R.style.TitleTextGray)
            backgroundRes(R.drawable.button_curvy_corners)
        }
        isNextDisabled = false
    }

    private fun setObservers() {
        viewModel.modelBrowsingLiveData.observe(
            viewLifecycleOwner,
            Observer(::updateBrowsingFittingUI)
        )
        viewModel.modelNavigationShape.observe(viewLifecycleOwner, Observer(::navigatesToShape))
        viewModel.modelReset.observe(viewLifecycleOwner, Observer {  resetFittingScreen() })
    }

    private fun resetFittingScreen() {
        textResetSkip.gone()
        buttonNext.style {
            add(R.style.BrowseNextDisable)
            backgroundRes(R.drawable.browse_next_disable)
        }
        adapter.clearSelection()
        isNextDisabled = true
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
}