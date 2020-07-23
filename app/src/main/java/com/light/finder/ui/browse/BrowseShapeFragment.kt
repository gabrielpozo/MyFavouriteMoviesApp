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
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.model.ShapeBrowsing
import com.light.finder.R
import com.light.finder.di.modules.submodules.BrowseShapeComponent
import com.light.finder.di.modules.submodules.BrowseShapeModule
import com.light.finder.extensions.getViewModel
import com.light.finder.extensions.gone
import com.light.finder.extensions.visible
import com.light.finder.ui.adapters.BrowseFittingAdapter
import com.light.finder.ui.adapters.BrowseShapeAdapter
import com.light.presentation.viewmodels.BrowseShapeViewModel
import kotlinx.android.synthetic.main.fragment_browse_shape.*
import kotlinx.android.synthetic.main.layout_browse_loading.*

class BrowseShapeFragment : BaseFilteringFragment() {

    companion object {
        const val SHAPE_ID_KEY = "BrowseShapeFragment::id"
    }

    private lateinit var component: BrowseShapeComponent
    private lateinit var adapter: BrowseShapeAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val viewModel: BrowseShapeViewModel by lazy { getViewModel { component.browseShapeViewModel } }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_browse_shape, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component = browseComponent.plus(BrowseShapeModule())
        }

        arguments?.let { bundle ->
            bundle.getInt(SHAPE_ID_KEY).let { productBaseId ->
                viewModel.onRequestFilteringShapes(productBaseId)
            }
        }

        textReset.paintFlags = textReset.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textSkip.paintFlags = textSkip.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textReset.setOnClickListener {

        }
        textSkip.setOnClickListener {

        }

        setAdapter()
        setBottomSheetBehaviour()
        setObservers()
    }

    private fun setBottomSheetBehaviour() {
        val bottomSheetLayout = view?.findViewById<NestedScrollView>(R.id.bottomSheetLayoutBrowse)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        // avoid unwanted scroll when bottom sheet collapsed
        ViewCompat.setNestedScrollingEnabled(recyclerViewShape, false)

        context?.let {
            val displayMetrics = it.resources.displayMetrics
            val dpHeight = displayMetrics.heightPixels
            bottomSheetBehavior.peekHeight = (dpHeight / 1.5).toInt()

        }
    }

    private fun setObservers() {
        viewModel.modelBrowsingLiveData.observe(
            viewLifecycleOwner, Observer(::updateBrowsingShapeUI)
        )
        //viewModel.modelNavigationShape.observe(viewLifecycleOwner, Observer(::navigatesToShape))
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

    private fun showLoading(){
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

    private fun setAdapter() {
        adapter = BrowseShapeAdapter(
            viewModel::onShapeClick
        )
        adapter.setHasStableIds(true)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewShape.layoutManager = layoutManager
        recyclerViewShape.adapter = adapter
    }
}