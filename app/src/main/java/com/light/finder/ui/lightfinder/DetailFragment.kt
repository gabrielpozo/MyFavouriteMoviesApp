package com.light.finder.ui.lightfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.light.domain.model.Product
import com.light.finder.R
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.di.modules.DetailComponent
import com.light.finder.di.modules.DetailModule
import com.light.finder.extensions.app
import com.light.finder.extensions.deparcelizeCategory
import com.light.finder.extensions.getViewModel
import com.light.finder.ui.adapters.DetailViewPagerAdapter
import com.light.presentation.viewmodels.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    companion object {
        const val PRODUCTS_ID_KEY = "ProductsFragment::id"
    }

    private lateinit var component: DetailComponent
    private val viewModel: DetailViewModel by lazy { getViewModel { component.detailViewModel } }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            component = app.applicationComponent.plus(DetailModule())
        } ?: throw Exception("Invalid Activity")
        setViewPager()
        setBottomSheet()
        arguments?.let { bundle ->
            bundle.getParcelable<CategoryParcelable>(PRODUCTS_ID_KEY)
                ?.let { categoryParcelable ->
                    viewModel.onRetrieveProductsAndFilters(categoryParcelable.deparcelizeCategory())
                }
            observeElements()
        }
    }

    private fun setBottomSheet() {
        val bottomSheet = ProductDetailBottomSheet()
        // bottomSheet.show(supportFragmentManager(), "exampleBottomSheet")
        //todo set bottomsheet
    }

    private fun setViewPager() {
        //todo set viewpager with images
        //viewPagerDetail.adapter = DetailViewPagerAdapter(requireContext(), imagesArray)
        //dotsIndicator.setViewPager(viewPagerDetail)
    }

    private fun observeElements() {
        //viewModel.productDetails.observe(viewLifecycleOwner, Observer(::setProductDetails))
        // viewModel.dataFilterButtons.observe(viewLifecycleOwner, Observer(::updateFilters))
    }

}

