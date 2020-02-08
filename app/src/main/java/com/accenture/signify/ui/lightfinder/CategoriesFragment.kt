package com.accenture.signify.ui.lightfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.accenture.domain.model.Message
import com.accenture.presentation.viewmodels.CategoryViewModel
import com.accenture.signify.R
import com.accenture.signify.di.modules.CategoriesComponent
import com.accenture.signify.di.modules.CategoriesModule
import com.accenture.signify.extensions.app
import com.accenture.signify.extensions.getViewModel
import com.accenture.signify.extensions.parcelize
import com.accenture.signify.ui.adapter.CategoriesAdapter
import kotlinx.android.synthetic.main.fragment_lightfinder.*

class CategoriesFragment : Fragment() {

    private lateinit var component: CategoriesComponent
    private lateinit var navController: NavController
    private val viewModel: CategoryViewModel by lazy { getViewModel { component.categoryViewModel } }
    private lateinit var adapter: CategoriesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lightfinder, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            component = app.applicationComponent.plus(CategoriesModule())
        } ?: throw Exception("Invalid Activity")

        navController = view.findNavController()

        initAdapter()

        viewModel.model.observe(this, Observer(::updateUi))
    }

    private fun updateUi(model: CategoryViewModel.UiModel) {
        progress.visibility =
            if (model is CategoryViewModel.UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is CategoryViewModel.UiModel.RequestMessages -> {
                viewModel.onRequestCategoriesMessages()
            }
            is CategoryViewModel.UiModel.Content -> updateData(model.messages)
            is CategoryViewModel.UiModel.Navigation -> {
                   navController.navigate(
                       R.id.action_categoriesFragment_to_productsFragment,
                       bundleOf(ProductsFragment.PRODUCTS_CATEGORY_ID_KEY to model.category.parcelize())
                   )
            }
        }
    }

    private fun initAdapter() {
        adapter = CategoriesAdapter(viewModel::onCategoryClicked)
        rv.adapter = adapter
    }

    private fun updateData(messages: List<Message>) {
        adapter.categories = messages[0].categories
    }

}