package com.accenture.signify.ui.lightfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.accenture.domain.model.Category
import com.accenture.domain.model.Message
import com.accenture.presentation.viewmodels.CategoryViewModel
import com.accenture.signify.R
import com.accenture.signify.di.modules.CategoriesComponent
import com.accenture.signify.di.modules.CategoriesModule
import com.accenture.signify.extensions.app
import com.accenture.signify.extensions.getViewModel
import com.accenture.signify.extensions.parcelize
import com.accenture.signify.ui.adapters.CategoriesAdapter
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() {

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

        initAdapter()

        viewModel.model.observe(this, Observer(::updateUI))
    }

    private fun updateUI(model: CategoryViewModel.UiModel) {
        progress.visibility = if (model is CategoryViewModel.UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is CategoryViewModel.UiModel.RequestMessages -> {
                viewModel.onRequestCategoriesMessages()
            }
            is CategoryViewModel.UiModel.Content -> updateData(model.messages)
        }
    }

    private fun initAdapter() {
        adapter = CategoriesAdapter(::navigateToProductList)
        rvCategories.adapter = adapter
    }

    private fun navigateToProductList(category: Category){
        findNavController().navigate(
            R.id.action_categoriesFragment_to_productsFragment,
            bundleOf(ProductsFragment.PRODUCTS_ID_KEY to category.parcelize())
        )
    }

    private fun updateData(messages: List<Message>) {
        adapter.categories = messages[0].categories
    }

}