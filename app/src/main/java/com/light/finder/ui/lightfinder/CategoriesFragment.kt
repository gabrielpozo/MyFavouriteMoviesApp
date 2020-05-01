package com.light.finder.ui.lightfinder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.domain.model.Category
import com.light.finder.CameraActivity
import com.light.finder.R
import com.light.finder.common.VisibilityCallBack
import com.light.finder.data.source.remote.MessageParcelable
import com.light.finder.di.modules.CategoriesComponent
import com.light.finder.di.modules.CategoriesModule
import com.light.finder.extensions.*
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.CategoriesAdapter
import com.light.presentation.common.Event
import com.light.presentation.viewmodels.CategoryViewModel
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : BaseFragment() {

    companion object {
        const val CATEGORIES_ID_KEY = "CategoriesFragment::id"
    }

    private lateinit var component: CategoriesComponent
    private val viewModel: CategoryViewModel by lazy { getViewModel { component.categoryViewModel } }
    private lateinit var adapter: CategoriesAdapter
    private lateinit var visibilityCallBack: VisibilityCallBack

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            visibilityCallBack = context as VisibilityCallBack
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            component = lightFinderComponent.plus(CategoriesModule())
        } ?: throw Exception("Invalid Activity")

        arguments?.let { bundle ->
            bundle.getParcelable<MessageParcelable>(CATEGORIES_ID_KEY)
                ?.let { messageParcelable ->
                    viewModel.onRetrieveCategories(messageParcelable.deparcelizeMessage())
                }

            viewModel.model.observe(viewLifecycleOwner, Observer { uiModel -> updateUI(uiModel) })
        }

        navigationObserver()
        initAdapter()
    }


    override fun onResume() {
        super.onResume()
        visibilityCallBack.onVisibilityChanged(false)
    }

    private fun navigationObserver() {
        viewModel.modelNavigation.observe(viewLifecycleOwner, Observer(::navigateToProductList))
    }

    private fun updateUI(model: CategoryViewModel.Content) {
        updateData(model.messages)
    }

    private fun initAdapter() {
        adapter = CategoriesAdapter(viewModel::onCategoryClick)
        rvCategories.adapter = adapter
    }

    private fun navigateToProductList(navigationModel: Event<CategoryViewModel.NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            screenNavigator.navigateToDetailScreen(navModel.category)
        }
    }

    private fun updateData(categories: List<Category>) {
        textViewResults.text = if (categories.size == 1) {
            getString(R.string.text_result).getIntFormatter(categories.size)
        } else {
            getString(R.string.text_results).getIntFormatter(categories.size)
        }
        textViewBulbType.text =
            getString(R.string.light_bulb_recognised_as).getStringFormatter(categories[0].categoryProductBase)

        adapter.categories = categories
    }

}