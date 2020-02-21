package com.light.finder.ui.lightfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.light.domain.model.Message
import com.light.presentation.viewmodels.CategoryViewModel
import com.light.finder.R
import com.light.finder.di.modules.CategoriesComponent
import com.light.finder.di.modules.CategoriesModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.finder.extensions.newInstance
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.CategoriesAdapter
import com.light.presentation.common.Event
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : BaseFragment() {

    companion object {
        const val CATEGORIES_ID_KEY = "CategoriesFragment::id"
    }

    private lateinit var component: CategoriesComponent
    private val viewModel: CategoryViewModel by lazy { getViewModel { component.categoryViewModel } }
    private lateinit var adapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            component = app.applicationComponent.plus(CategoriesModule())
        } ?: throw Exception("Invalid Activity")

        arguments?.let { bundle ->
            bundle.getString(CATEGORIES_ID_KEY)
                ?.let { base64 ->
                    viewModel.model.observe(this, Observer { uiModel -> updateUI(uiModel, base64) })
                }
        }

        navigationObserver()
        initAdapter()
    }

    private fun navigationObserver() {
        viewModel.modelNavigation.observe(viewLifecycleOwner, Observer(::navigateToProductList))
    }


    private fun updateUI(model: CategoryViewModel.UiModel, base64: String) {
        progress.visibility =
            if (model is CategoryViewModel.UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is CategoryViewModel.UiModel.RequestMessages -> viewModel.onRequestCategoriesMessages(
                base64
            )
            is CategoryViewModel.UiModel.Content -> updateData(model.messages)

        }
    }

    private fun initAdapter() {
        adapter = CategoriesAdapter(viewModel::onCategoryClick)
        rvCategories.adapter = adapter
    }

    private fun navigateToProductList(navigationModel: Event<CategoryViewModel.NavigationModel>) {
        navigationModel.getContentIfNotHandled()?.let { navModel ->
            mFragmentNavigation.pushFragment(ProductsFragment.newInstance(navModel.category))
        }

    }

    private fun updateData(messages: List<Message>) {
        adapter.categories = messages[0].categories
    }

}