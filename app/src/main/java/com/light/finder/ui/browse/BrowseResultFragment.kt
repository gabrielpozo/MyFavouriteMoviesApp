package com.light.finder.ui.browse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.domain.model.Message
import com.light.finder.R
import com.light.finder.data.source.local.LocalPreferenceDataSourceImpl
import com.light.finder.data.source.remote.ChoiceBrowsingParcelable
import com.light.finder.di.modules.submodules.BrowseResultComponent
import com.light.finder.di.modules.submodules.BrowseResultModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.BrowseResultAdapter
import com.light.finder.ui.filter.FilterLightFinderActivity
import com.light.finder.ui.filter.FilterLightFinderActivity.Companion.SORT_ID
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.BrowseResultViewModel
import com.light.source.local.LocalPreferenceDataSource
import kotlinx.android.synthetic.main.browse_results_header.*
import kotlinx.android.synthetic.main.fragment_browse_result.*

class BrowseResultFragment : BaseFragment() {

    companion object {
        const val CATEGORIES_BROWSE_ID_KEY = "BrowseResultFragment::id"
        const val BROWSE_RESULT_SCREEN_TAG = "BrowseResults"
        const val FILTER_REQUEST_CODE = 1
        const val SORT_SELECTION = "sortId"
    }

    private lateinit var component: BrowseResultComponent
    private var sortId = 2131362146
    private val viewModel: BrowseResultViewModel by lazy { getViewModel { component.browseResultViewModel } }
    private lateinit var adapter: BrowseResultAdapter
    private val localPreferences: LocalPreferenceDataSource by lazy {
        LocalPreferenceDataSourceImpl(
            requireContext()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_browse_result, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            component = lightFinderComponent.plus(BrowseResultModule())
        } ?: throw Exception("Invalid Activity")

        arguments?.let { bundle ->
            bundle.getParcelableArrayList<ChoiceBrowsingParcelable>(CATEGORIES_BROWSE_ID_KEY)
                ?.let { choiceBrowsingProducts ->
                    viewModel.onRetrieveShapeProducts(choiceBrowsingProducts.deparcelizeChoiceBrowsingList())
                }

            viewModel.model.observe(viewLifecycleOwner, Observer { uiModel -> updateUI(uiModel) })
        }

        filterButton.setSafeOnClickListener {
            viewModel.onFilterClick(FILTER_REQUEST_CODE)
        }




        navigationObserver()
        filterObserver()
        sortObserver()
    }

    private fun setEditBrowseTypes(message: Message) {
        val product = message.categories[0]
        textBrowseTypes.text =
            product.categoryProductBase + "\u2022" + product.categoryShape + " ..." + product.categoryName
    }

    private fun filterObserver() {
        viewModel.modelFilter.observe(viewLifecycleOwner, Observer(::navigateToFilter))
    }

    private fun sortObserver() {
        viewModel.modelSort.observe(viewLifecycleOwner, Observer(::sortResults))
    }

    private fun navigateToFilter(filterModel: Event<BrowseResultViewModel.FilterModel>?) {
        filterModel?.getContentIfNotHandled()?.let { navModel ->
            val intent = Intent(context, FilterLightFinderActivity::class.java)
            intent.putExtra(SORT_SELECTION, sortId)
            startActivityForResult(intent, navModel.requestCode)
            activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.stay)
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.trackScreen(this@BrowseResultFragment, activity, BROWSE_RESULT_SCREEN_TAG)
    }

    private fun navigationObserver() {
        viewModel.modelNavigation.observe(viewLifecycleOwner, Observer(::navigateToProductList))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            sortId = data?.getIntExtra(
                SORT_ID,
                2131362146
            ) ?: -1
        }

        viewModel.onSortResults(sortId)
    }

    private fun sortResults(sortModel: BrowseResultViewModel.SortModel?) {
        sortModel?.sortId?.let { viewModel.onSortCategories(it) }
    }


    private fun updateUI(model: BrowseResultViewModel.ResultBrowse) {
        when (model) {
            is BrowseResultViewModel.ResultBrowse.Content -> {
                updateAdapter(model.message)
                setEditBrowseTypes(model.message)
            }

            is BrowseResultViewModel.ResultBrowse.SortedContent -> {
                adapter.categories = model.categories
                adapter.notifyDataSetChanged()
                rvCategories.scrollToPosition(0)
            }

            is BrowseResultViewModel.ResultBrowse.NoResult -> {
                rvCategories.gone()
                //browseNoResultsView.visible()
                updateNoResultsData(model.message)
            }
        }
    }

    private fun navigateToProductList(navigationModel: Event<BrowseResultViewModel.NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            screenNavigator.navigateToDetailScreen(navModel.category)
        }
    }

    private fun updateNoResultsData(message: Message) {
        when (message.categories.size) {
            1 -> {
                textViewResults.text =
                    getString(R.string.text_result).getIntFormatter(message.categories.size)
            }
            else -> {
                textViewResults.text =
                    getString(R.string.text_results).getIntFormatter(message.categories.size)
            }
        }

        textViewFitting.text = getString(R.string.based_on_result_fitting).format(
            message.shapeIdentified
        )
    }


    private fun updateAdapter(message: Message) {
        setAdapter(message)
        adapter.categories = message.categories
    }


    private fun setAdapter(message: Message) {
        adapter = BrowseResultAdapter(
            viewModel::onCategoryClick,
            localPreferences.loadLegendCctFilterNames(),
            localPreferences.loadLegendFinishFilterNames(),
            localPreferences.loadProductCategoryName(),
            message.shapeIdentified
        )
        rvCategories.itemAnimator = null
        rvCategories.adapter = adapter
    }

}

