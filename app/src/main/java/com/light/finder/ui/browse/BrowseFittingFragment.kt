package com.light.finder.ui.browse


import android.animation.ValueAnimator
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.light.domain.model.FittingBrowsing
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
import kotlinx.android.synthetic.main.activity_browse.*
import kotlinx.android.synthetic.main.fragment_browse_fitting.*
import kotlinx.android.synthetic.main.layout_browse_loading.*


class BrowseFittingFragment : BaseFilteringFragment() {

    private lateinit var component: BrowsingFittingComponent
    private lateinit var adapter: BrowseFittingAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

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

        setBottomSheetBehaviour()
        setObservers()
        setFittingListeners()
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

    private fun setAdapter(productBrowsingList: List<FittingBrowsing>) {
        adapter = BrowseFittingAdapter(
            viewModel::onFittingClick,
            productBrowsingList
        )
        val layoutManager = GridLayoutManager(context, 3)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerViewFitting.addItemDecoration(FittingItemDecoration(3,spacingInPixels,true))
        recyclerViewFitting.layoutManager = layoutManager
        recyclerViewFitting.adapter = adapter
    }

    private fun setObservers() {
        viewModel.modelBrowsingLiveData.observe(
            viewLifecycleOwner,
            Observer(::updateBrowsingFittingUI)
        )
        viewModel.modelNavigationShape.observe(viewLifecycleOwner, Observer(::navigatesToShape))
    }

    private fun setFittingListeners() {
      /* buttonSearch.setOnClickListener {
            viewModel.onSearchButtonPressed()
        }*/
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

    private fun showFittings(producFittingList: List<FittingBrowsing>) {
        Log.d("Gabriel","Browse Fitting Fragnment: $producFittingList")
        recyclerViewFitting.visible()
        browseError.gone()
        browseLoading.gone()
        setAdapter(producFittingList)
        //TODO add the list here to the adapter (productBrowsingList)
    }
}