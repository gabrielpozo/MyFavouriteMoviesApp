package com.light.finder.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.domain.model.Message
import com.light.finder.R
import com.light.finder.data.source.local.LocalPreferenceDataSourceImpl
import com.light.finder.data.source.remote.ShapeBrowsingParcelable
import com.light.finder.di.modules.submodules.BrowseResultComponent
import com.light.finder.di.modules.submodules.BrowseResultModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.BrowseResultAdapter
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.BrowseResultViewModel
import com.light.source.local.LocalPreferenceDataSource
import kotlinx.android.synthetic.main.fragment_browse_result.*
import kotlinx.android.synthetic.main.fragment_category_result.*
import kotlinx.android.synthetic.main.fragment_category_result.rvCategories
import kotlinx.android.synthetic.main.fragment_category_result.textViewNoResultSubTitle
import kotlinx.android.synthetic.main.fragment_category_result.textViewNoResultTitle

class BrowseResultFragment : BaseFragment() {

    companion object {
        const val CATEGORIES_BROWSE_ID_KEY = "BrowseResultFragment::id"
    }

    private lateinit var component: BrowseResultComponent
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
            bundle.getParcelableArrayList<ShapeBrowsingParcelable>(CATEGORIES_BROWSE_ID_KEY)
                ?.let { shapeBrowsingProducts ->
                    viewModel.onRetrieveShapeProducts(shapeBrowsingProducts.deParcelizeBrowsingList())
                }

            viewModel.model.observe(viewLifecycleOwner, Observer { uiModel -> updateUI(uiModel) })
        }

        navigationObserver()
    }


    private fun navigationObserver() {
        viewModel.modelNavigation.observe(viewLifecycleOwner, Observer(::navigateToProductList))
    }

    private fun updateUI(model: BrowseResultViewModel.ResultBrowse) {
        when (model) {
            is BrowseResultViewModel.ResultBrowse.Content -> {
                updateData(model.message)
                updateAdapter(model.message)


            }
            is BrowseResultViewModel.ResultBrowse.NoResult -> {
                rvCategories.gone()
                textViewNoResultSubTitle.visible()
                textViewNoResultTitle.visible()
                textViewNoResultSubTitle.visible()

                updateData(model.message)
            }
        }
    }

    private fun navigateToProductList(navigationModel: Event<BrowseResultViewModel.NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            screenNavigator.navigateToDetailScreen(navModel.category)
        }
    }

    private fun updateData(message: Message) {
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

        textViewBulbType.invisible()
        textViewFitting.text = getString(R.string.based_on_result_fitting).format(
            getFormFactorBaseIdTagName(
                message.baseIdentified.toInt(),
                localPreferences.loadFormFactorIBaseIdLegendTags()
            )
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
            localPreferences.loadFormFactorLegendTags(),
            localPreferences.loadLegendFinishFilterNames(),
            localPreferences.loadFormFactorIdLegendTags(),
            localPreferences.loadProductCategoryName(),
            message.shapeIdentified
        )
        rvCategories.adapter = adapter

    }

}