package com.accenture.signify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.accenture.domain.model.Message
import com.accenture.presentation.viewmodels.CategoryViewModel
import com.accenture.signify.R
import com.accenture.signify.di.modules.CategoriesComponent
import com.accenture.signify.di.modules.CategoriesModule
import com.accenture.signify.extensions.app
import com.accenture.signify.extensions.getViewModel

class CategoriesFragment : Fragment(){

    private lateinit var component: CategoriesComponent
    private val viewModel: CategoryViewModel by lazy { getViewModel { component.categoryViewModel } }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.support_simple_spinner_dropdown_item, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            component = app.applicationComponent.plus(CategoriesModule())
        } ?: throw Exception("Invalid Activity")



    }

    private fun updateUI(model: CategoryViewModel.UiModel) {
        when (model) {
            is CategoryViewModel.UiModel.Loading -> {}
            is CategoryViewModel.UiModel.RequestMessages -> {}
            is CategoryViewModel.UiModel.Content -> updateData(model.messages)
        }
    }

    private fun updateData(messages: List<Message>) {
        initAdapter(messages)
    }

    private fun initAdapter(items: List<Message>) {

    }
}