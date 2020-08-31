package com.light.finder.ui.browse

import android.animation.ValueAnimator
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.backgroundRes
import com.airbnb.paris.extensions.style
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.light.domain.model.ShapeBrowsing
import com.light.finder.R
import com.light.finder.data.source.remote.FormFactorTypeBaseIdParcelable
import com.light.finder.di.modules.submodules.BrowseShapeComponent
import com.light.finder.di.modules.submodules.BrowseShapeModule
import com.light.finder.extensions.*
import com.light.finder.ui.adapters.BrowseShapeAdapter
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.BrowseShapeViewModel
import kotlinx.android.synthetic.main.fragment_browse_shape.*
import kotlinx.android.synthetic.main.layout_browse_loading.*

class BrowseShapeFragment : BaseFilteringFragment() {

    companion object {
        const val SHAPE_ID_KEY = "BrowseShapeFragment::id"
        const val RESTORED_STATE = 4
    }

    private lateinit var component: BrowseShapeComponent
    private lateinit var adapter: BrowseShapeAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var rootview: View? = null


    private val viewModel: BrowseShapeViewModel by lazy { getViewModel { component.browseShapeViewModel } }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootview == null) {
            rootview = inflater.inflate(R.layout.fragment_browse_shape, container, false)
        }
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component = browseComponent.plus(BrowseShapeModule())
        }

        arguments?.let { bundle ->
            bundle.getParcelable<FormFactorTypeBaseIdParcelable>(SHAPE_ID_KEY)
                ?.let { productBaseId ->
                    viewModel.onRequestFilteringShapes(productBaseId.deparcelizeFormFactor())
                }
        }

        textReset.paintFlags = textReset.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textSkip.paintFlags = textSkip.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textReset.setOnClickListener {
            viewModel.onResetButtonPressed()
        }
        textSkip.setOnClickListener {
            viewModel.onSkipButtonClicked()
        }
        buttonSearch.setOnClickListener {
            viewModel.onSearchButtonClicked()
        }

        setAdapter()
        setBottomSheetBehaviour()
        setObservers()
    }

    private fun setAdapter() {
        adapter = BrowseShapeAdapter(
            viewModel::onShapeClick
        )
        adapter.setHasStableIds(true)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewShape.layoutManager = layoutManager
        recyclerViewShape.adapter = adapter
        recyclerViewShape.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //it is scrolling up
                if (dy > 0) {
                    line_divider.visible()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager != null && layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    line_divider.invisible()
                }
            }
        })
    }

    private fun setBottomSheetBehaviour() {
        val bottomSheetLayout = view?.findViewById<LinearLayout>(R.id.bottomSheetLayoutBrowse)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        context?.let {
            val displayMetrics = it.resources.displayMetrics
            val dpHeight = displayMetrics.heightPixels
            bottomSheetBehavior.peekHeight = (dpHeight * 0.66).toInt()
            bottomSheetBehavior.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {}

                override fun onStateChanged(p0: View, state: Int) {}
            })
        }
    }

    private fun setObservers() {
        viewModel.modelBrowsingLiveData.observe(
            viewLifecycleOwner, Observer(::updateBrowsingShapeUI)
        )
        viewModel.modelBottomStatus.observe(viewLifecycleOwner, Observer(::updateStatusBottomBar))
        viewModel.modelNavigationToResult.observe(
            viewLifecycleOwner,
            Observer(::navigatesToCategoriesChoice)
        )
    }


    private fun updateBrowsingShapeUI(modelBrowse: BrowseShapeViewModel.UiBrowsingShapeModel) {
        when (modelBrowse) {
            is BrowseShapeViewModel.UiBrowsingShapeModel.SuccessRequestStatus -> {
                showShapes(modelBrowse.productBrowsingList)
            }

            is BrowseShapeViewModel.UiBrowsingShapeModel.LoadingStatus -> {
                showLoading()
            }
        }
    }

    private fun updateStatusBottomBar(statusBottomBar: BrowseShapeViewModel.StatusBottomBar?) {
        when (statusBottomBar) {
            is BrowseShapeViewModel.StatusBottomBar.ResetShape -> {
                resetShapeSelection()
            }
            is BrowseShapeViewModel.StatusBottomBar.ShapeClicked -> {
                settingFilterShapeSelected()
            }
            is BrowseShapeViewModel.StatusBottomBar.NoButtonsClicked -> {
                resetShapeSelection()
            }
        }

    }

    private fun settingFilterShapeSelected() {
        buttonSearch.style {
            add(R.style.TitleTextGray)
            backgroundRes(R.drawable.button_curvy_corners)
        }
        textReset.visible()
        textSkip.gone()
    }

    private fun resetShapeSelection() {
        textSkip.visible()
        textReset.gone()
        buttonSearch.style {
            add(R.style.BrowseNextDisable)
            backgroundRes(R.drawable.browse_next_disable)
        }

        adapter.clearSelection()
    }

    private fun showLoading() {
        recyclerViewShape.gone()
        browseLoadingShape.visible()
        with(browseLoadingAnimation) {
            playAnimation()
            repeatCount = ValueAnimator.INFINITE
        }
    }

    private fun showShapes(productFittingList: List<ShapeBrowsing>) {
        recyclerViewShape.visible()
        browseLoadingShape.gone()
        adapter.setShapeProductList(productFittingList)
    }

    private fun navigatesToCategoriesChoice(modelNavigationEvent: Event<BrowseShapeViewModel.NavigationToResults>) {
        modelNavigationEvent.getContentIfNotHandled()?.let { browseNavigation ->
            screenFilteringNavigator.navigateToBrowsingChoiceScreen(browseNavigation.productsShapeSelected)
        }
    }
}