package com.accenture.signify.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.accenture.domain.model.Message
import com.accenture.presentation.viewmodels.CategoryViewModel
import com.accenture.signify.R
import com.accenture.signify.di.modules.CategoriesComponent
import com.accenture.signify.di.modules.CategoriesModule
import com.accenture.signify.extensions.app
import com.accenture.signify.extensions.getViewModel
import com.accenture.signify.ui.adapter.CategoriesAdapter
import kotlinx.android.synthetic.main.fragment_lightfinder.*

class LightFinderFragment : Fragment() {

    private lateinit var component: CategoriesComponent
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

        initAdapter()

        viewModel.model.observe(this, Observer(::updateUi))
    }

    private fun updateUi(model: CategoryViewModel.UiModel) {
        progress.visibility = if (model is CategoryViewModel.UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is CategoryViewModel.UiModel.RequestMessages -> { viewModel.onRequestCategoriesMessages() }
            is CategoryViewModel.UiModel.Content -> updateData(model.messages)
            is CategoryViewModel.UiModel.Navigation -> {
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