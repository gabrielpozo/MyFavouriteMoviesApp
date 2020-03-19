package com.light.finder.ui.lightfinder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.light.finder.R
import com.light.finder.common.VisibilityCallBack
import com.light.finder.data.source.remote.MessageParcelable
import com.light.finder.di.modules.ProductsOptionsComponent
import com.light.finder.di.modules.ProductsOptionsModule
import com.light.finder.extensions.app
import com.light.finder.extensions.getViewModel
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.adapters.CategoriesAdapter
import com.light.presentation.viewmodels.ProductsOptionsViewModel
import java.lang.ClassCastException


class ProductOptionsFragment : BaseFragment() {

    companion object {
        const val PRODUCTS_OPTIONS_ID_KEY = "ProductsOptionsFragment::id"
    }




    private lateinit var component: ProductsOptionsComponent
    private val viewModel: ProductsOptionsViewModel by lazy { getViewModel { component.productsOptionsViewModel } }
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
            component = app.applicationComponent.plus(ProductsOptionsModule())
        } ?: throw Exception("Invalid Activity")

        arguments?.let { bundle ->
            bundle.getParcelable<MessageParcelable>(CategoriesFragment.CATEGORIES_ID_KEY)
                ?.let { messageParcelable ->
                   // viewModel.onRetrieveCategories(messageParcelable.deparcelizeMessage())
                }

            //viewModel.model.observe(viewLifecycleOwner, Observer { uiModel -> updateUI(uiModel) })
        }

        //navigationObserver()
       // initAdapter()
    }
}