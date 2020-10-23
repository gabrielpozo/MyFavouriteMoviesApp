package com.light.finder.ui.browse


import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.backgroundRes
import com.airbnb.paris.extensions.style
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.light.domain.model.FormFactorTypeBaseId
import com.light.finder.R
import com.light.finder.di.modules.submodules.BrowseFittingModule
import com.light.finder.di.modules.submodules.BrowsingFittingComponent
import com.light.finder.extensions.*
import com.light.finder.ui.adapters.BrowseFittingAdapter
import com.light.finder.ui.common.itemdecoration.FittingItemDecoration
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

    private val BROWSE_SCREEN_TAG = "BrowseChooseFitting"
    private val viewModel: BrowseFittingViewModel by lazy { getViewModel { component.browseFittingViewModel } }

    companion object {
        const val spaceInDp = 30
        const val FITTING_EDIT_ID_KEY = "FITTING_EDIT_ID_KEY"
    }


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

        arguments?.let {
            viewModel.onRequestFormFactorFromEditBrowse()
        } ?: run {
            viewModel.onRequestBrowsingProducts()
        }

        buttonNext.setSafeOnClickListener {
            viewModel.onNextButtonPressed()
        }


        buttonRefresh.setSafeOnClickListener {
            viewModel.onRequestBrowsingProducts()
        }

        setAdapter()
        setBottomSheetBehaviour()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.trackScreen(this@BrowseFittingFragment, activity, BROWSE_SCREEN_TAG)
    }


    private fun setBottomSheetBehaviour() {
        val bottomSheetLayout = view?.findViewById<LinearLayout>(R.id.bottomSheetLayoutBrowse)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)


        context?.let {
            val displayMetrics = it.resources.displayMetrics
            val dpHeight = displayMetrics.heightPixels

            bottomSheetBehavior.peekHeight = (dpHeight * 0.66).toInt()
        }
    }

    private fun setAdapter() {
        adapter = BrowseFittingAdapter(
            viewModel::onFittingClick
        )

        val layoutManager = GridLayoutManager(context, 3)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        recyclerViewFitting.addItemDecoration(FittingItemDecoration(context!!, R.dimen.spacing))
        recyclerViewFitting.itemAnimator = null
        recyclerViewFitting.layoutManager = layoutManager
        recyclerViewFitting.adapter = adapter
        recyclerViewFitting.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.computeVerticalScrollOffset()
                        .pxToDp(density) >= spaceInDp
                ) {
                    line_divider_fitting.visible()
                }
                if (recyclerView.computeVerticalScrollOffset()
                        .pxToDp(density) < spaceInDp
                ) {
                    line_divider_fitting.invisible()
                }
            }
        })
    }


    private fun setObservers() {
        viewModel.modelBrowsingLiveData.observe(
            viewLifecycleOwner,
            Observer(::updateBrowsingFittingUI)
        )
        viewModel.modelNavigationShape.observe(viewLifecycleOwner, Observer(::navigatesToShape))
        viewModel.modelBottomStatus.observe(viewLifecycleOwner, Observer(::updateStatusBottomBar))
        viewModel.modelFittingClickStatus.observe(
            viewLifecycleOwner,
            Observer { updateClickStatus() })

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

    private fun updateStatusBottomBar(modelEvent: Event<BrowseFittingViewModel.ResetFitting>) {
        modelEvent.getContentIfNotHandled()?.let { _ ->
            resetFittingSelection()
        }
    }

    private fun updateClickStatus() {
        settingFilterSelected()
    }

    private fun navigatesToShape(modelNavigation: Event<BrowseFittingViewModel.NavigationToShapeFiltering>) {
        modelNavigation.getContentIfNotHandled()?.let { model ->
            screenFilteringNavigator.navigateToBrowsingShapeScreen(model.productBaseFormFactor)
        }
    }

    private fun showLoading() {
        buttonNext.gone()
        list_container.gone()
        browseError.gone()
        browseLoading.visible()
        with(browseLoadingAnimation) {
            playAnimation()
            repeatCount = ValueAnimator.INFINITE
        }
    }

    private fun showError() {
        list_container.gone()
        browseError.visible()
        browseLoading.gone()
        buttonNext.gone()
    }

    private fun showFittings(productFittingList: List<FormFactorTypeBaseId>) {
        buttonNext.visible()
        list_container.visible()
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