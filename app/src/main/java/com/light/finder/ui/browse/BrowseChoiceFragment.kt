package com.light.finder.ui.browse

import android.animation.ValueAnimator
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.extensions.backgroundRes
import com.airbnb.paris.extensions.style
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.light.domain.model.ChoiceBrowsing
import com.light.finder.R
import com.light.finder.data.source.remote.ShapeBrowsingParcelable
import com.light.finder.di.modules.submodules.BrowseChoiceComponent
import com.light.finder.di.modules.submodules.BrowseChoiceModule
import com.light.finder.extensions.*
import com.light.finder.ui.adapters.BrowseChoiceAdapter
import com.light.finder.ui.browse.BrowseShapeFragment.Companion.SHAPE_BACK_CODE
import com.light.finder.ui.browse.BrowseShapeFragment.Companion.SHAPE_BACK_KEY
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.BrowseChoiceViewModel
import kotlinx.android.synthetic.main.fragment_browse_choice.*
import kotlinx.android.synthetic.main.layout_browse_loading.*

class BrowseChoiceFragment : BaseFilteringFragment(), BrowseExpandableStatus, IOnBackPressed {

    companion object {
        const val CHOICE_ID_KEY = "BrowseChoiceFragment::id"
        const val CHOICE_EDIT_ID_KEY = "BrowseChoiceEditFragment::id"

        const val CHOICE_NUMBER_KEY = 4
        const val spaceInDp = 26
    }

    private lateinit var component: BrowseChoiceComponent
    private lateinit var adapter: BrowseChoiceAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val viewModel: BrowseChoiceViewModel by lazy { getViewModel { component.browseChoiceViewModel } }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_browse_choice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component = browseComponent.plus(BrowseChoiceModule())
        }

        arguments?.let { bundle ->
            bundle.getParcelableArrayList<ShapeBrowsingParcelable>(CHOICE_ID_KEY)
                ?.let { shapeBrowsingProducts ->
                    viewModel.onRetrieveShapeProducts(shapeBrowsingProducts.deParcelizeBrowsingList())
                }

            bundle.getInt(CHOICE_EDIT_ID_KEY)
                .let { key ->
                    if (key == CHOICE_NUMBER_KEY) {
                        viewModel.onRetrieveChoiceProducts()
                    }
                }
        }


        textReset.paintFlags = textReset.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textSkip.paintFlags = textSkip.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textReset.setSafeOnClickListener {
            viewModel.onResetButtonPressed()
        }
        textSkip.setSafeOnClickListener {
            viewModel.onSkipButtonClicked()
        }
        buttonSearch.setSafeOnClickListener {
            viewModel.onSearchButtonClicked()
        }

        setAdapter()
        setBottomSheetBehaviour()
        setObservers()

    }

    override fun setExpandableChoiceSelection() {
        viewModel.onRetrieveChoiceProducts()
    }

    private fun setAdapter() {
        adapter = BrowseChoiceAdapter(
            viewModel::onChoiceClick
        )

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewChoice.itemAnimator = null
        recyclerViewChoice.layoutManager = layoutManager
        recyclerViewChoice.adapter = adapter
        recyclerViewChoice.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.computeVerticalScrollOffset()
                        .pxToDp(density) >= spaceInDp
                ) {
                    lineDividerCategoryChoice.visible()
                }
                if (recyclerView.computeVerticalScrollOffset()
                        .pxToDp(density) < spaceInDp && lineDividerCategoryChoice.isVisible
                ) {
                    lineDividerCategoryChoice.invisible()
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
        }
    }

    private fun setObservers() {
        viewModel.modelChoiceLiveData.observe(
            viewLifecycleOwner, Observer(::updateBrowsingChoiceUI)
        )
        viewModel.modelBottomStatus.observe(viewLifecycleOwner, Observer(::updateStatusBottomBar))
        viewModel.modelNavigationToResult.observe(
            viewLifecycleOwner,
            Observer(::navigatesToCategoriesResult)
        )
    }


    private fun updateBrowsingChoiceUI(modelChoiceBrowse: BrowseChoiceViewModel.UiBrowsingChoiceModel) {
        when (modelChoiceBrowse) {
            is BrowseChoiceViewModel.UiBrowsingChoiceModel.SuccessRequestStatus -> {
                showChoices(modelChoiceBrowse.productBrowsingList)
            }

            is BrowseChoiceViewModel.UiBrowsingChoiceModel.LoadingStatus -> {
                showLoading()
            }
        }
    }

    private fun updateStatusBottomBar(statusBottomBar: BrowseChoiceViewModel.StatusBottomBar?) {
        when (statusBottomBar) {
            is BrowseChoiceViewModel.StatusBottomBar.ResetChoice -> {
                resetChoiceSelection()
            }
            is BrowseChoiceViewModel.StatusBottomBar.ChoiceClicked -> {
                settingChoiceSelected()
            }
            is BrowseChoiceViewModel.StatusBottomBar.NoButtonsClicked -> {
                resetChoiceSelection()
            }
        }

    }

    private fun settingChoiceSelected() {
        buttonSearch.style {
            add(R.style.TitleTextGray)
            backgroundRes(R.drawable.button_curvy_corners)
        }
        textReset.visible()
        textSkip.gone()
    }

    private fun resetChoiceSelection() {
        textSkip.visible()
        textReset.gone()
        buttonSearch.style {
            add(R.style.BrowseNextDisable)
            backgroundRes(R.drawable.browse_next_disable)
        }
        adapter.clearSelection()
    }

    private fun showLoading() {
        recyclerViewChoice.gone()
        browseLoadingChoice.visible()
        with(browseLoadingAnimation) {
            playAnimation()
            repeatCount = ValueAnimator.INFINITE
        }
    }

    private fun showChoices(productChoiceList: List<ChoiceBrowsing>) {
        recyclerViewChoice.visible()
        browseLoadingChoice.gone()
        adapter.setChoiceProductList(productChoiceList)
    }

    //todo change with categories
    private fun navigatesToCategoriesResult(modelNavigationEvent: Event<BrowseChoiceViewModel.NavigationToResults>) {
        modelNavigationEvent.getContentIfNotHandled()?.let { browseNavigation ->
            screenFilteringNavigator.navigateToResultCategories(browseNavigation.productsChoiceSelected)
            firebaseAnalytics.logEventOnGoogleTagManager(getString(R.string.browse_applied)) {
                putString(
                    getString(R.string.base),
                    browseNavigation.productsChoiceSelected[0].baseNameFitting
                )
            }
        }
    }

    override fun onBackPressed() {
        //TODO move to screen navigation classs
        val intent = Intent()
        intent.putExtra(SHAPE_BACK_KEY, true)
        targetFragment?.onActivityResult(SHAPE_BACK_CODE, RESULT_OK, intent)
    }
}