package com.light.finder.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import com.light.finder.R
import com.light.finder.di.modules.submodules.BrowseShapeComponent
import com.light.finder.di.modules.submodules.BrowseShapeModule
import com.light.finder.extensions.getViewModel
import com.light.finder.ui.adapters.BrowseShapeDetailsLookup
import com.light.finder.ui.lightfinder.DetailFragment
import com.light.presentation.viewmodels.BrowseShapeViewModel
import kotlinx.android.synthetic.main.fragment_browse_shape.*

class BrowseShapeFragment : BaseFilteringFragment() {

    companion object {
        const val SHAPE_ID_KEY = "BrowseShapeFragment::id"
    }

    private val viewModel: BrowseShapeViewModel by lazy { getViewModel { component.browseShapeViewModel } }
    private lateinit var component: BrowseShapeComponent

    var tracker: SelectionTracker<Long>? = null

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
            bundle.getInt(DetailFragment.PRODUCTS_ID_KEY).let { productBaseId ->
                viewModel.onRequestFilteringShapes(productBaseId)
            }
        }

        setAdapter()
    }

    private fun setAdapter() {
        tracker = SelectionTracker.Builder<Long>(
            "shapeSelection",
            recyclerViewShape,
            StableIdKeyProvider(recyclerViewShape),
            BrowseShapeDetailsLookup(recyclerViewShape),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        //adapter.tracker = tracker
    }
}