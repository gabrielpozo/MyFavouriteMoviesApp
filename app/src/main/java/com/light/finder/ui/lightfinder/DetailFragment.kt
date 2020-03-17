package com.light.finder.ui.lightfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.light.finder.R
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.di.modules.DetailComponent
import com.light.finder.di.modules.DetailModule
import com.light.finder.extensions.app
import com.light.finder.extensions.deparcelizeCategory
import com.light.finder.extensions.getViewModel
import com.light.presentation.viewmodels.DetailViewModel
import kotlinx.android.synthetic.main.layout_detail_bottom_sheet.*





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
        val bundle = Bundle()
        /*val behavior = BottomSheetBehavior.from(bottomSheet.view)
        behavior.peekHeight = BottomSheetBehavior.STATE_EXPANDED*/
        //todo send actual values
        bundle.putString("test", "oh ya")
        bottomSheet.arguments = bundle
        bottomSheet.isCancelable = false
        bottomSheet.show(parentFragmentManager,"")
    }

    private fun setViewPager() {
        //todo set viewpager with images
        //viewPagerDetail.adapter = DetailViewPagerAdapter(requireContext(), imagesArray)
    }

    private fun observeElements() {
        //viewModel.productDetails.observe(viewLifecycleOwner, Observer(::setProductDetails))
        // viewModel.dataFilterButtons.observe(viewLifecycleOwner, Observer(::updateFilters))
    }

}

