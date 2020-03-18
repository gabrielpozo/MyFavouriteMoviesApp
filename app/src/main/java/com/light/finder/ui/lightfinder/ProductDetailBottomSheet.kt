package com.light.finder.ui.lightfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.light.domain.model.Product
import com.light.finder.R
import androidx.annotation.NonNull
import com.light.finder.data.source.remote.ProductParcelable
import com.light.finder.extensions.mapParcelableProductToDomain
import kotlinx.android.synthetic.main.layout_detail_bottom_sheet.*

class ProductDetailBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val PRODUCT_DETAIL_ID_KEY = "ProductDetailBottomSheet::id"
    }


    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.layout_detail_bottom_sheet, container, false)
        //todo set text
        return v
    }

    override fun getTheme(): Int  = R.style.BottomSheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            bundle.getParcelable<ProductParcelable>(PRODUCT_DETAIL_ID_KEY)
                ?.let { productParcelable ->
                    populateProductData(mapParcelableProductToDomain(productParcelable))
                }
            //setObservers()
        }


        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val dialog = dialog as BottomSheetDialog
                val bottomSheet =
                    dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.isHideable = false
                behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(@NonNull view: View, i: Int) {
                        behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
                    }

                    override fun onSlide(@NonNull view: View, v: Float) {

                    }
                })

                //todo fix peak height
                //behavior.peekHeight = BottomSheetBehavior.STATE_HALF_EXPANDED

                val newHeight = activity?.window?.decorView?.measuredHeight
                val viewGroupLayoutParams = bottomSheet.layoutParams
                viewGroupLayoutParams.height = newHeight ?: 0
                bottomSheet.layoutParams = viewGroupLayoutParams
            }
        })

    }

    private fun populateProductData(product: Product) {
        val parts = product.name.split(",")
        val pack = parts[parts.size - 2] + " " + parts[parts.size - 1]
        val isDimmable = if (product.dimmingCode == 0) "" else "Dimmable"
        val title = String.format(
            getString(R.string.product_title),
            product.categoryName,
            isDimmable,
            product.wattageReplaced,
            product.factorBase,
            product.factorShape,
            pack.replace("-", " ").replace("Pack", "pack")
        )

        val price = String.format(
            getString(R.string.price_detail),
            product.pricePack,
            product.priceLamp

        )

        val changeVariation = String.format(
            getString(R.string.change_variation),
            product.wattageReplaced,
            product.colorCctCode,
            product.finish
        )


        textViewDetailTitle.text = title.trim().replace(Regex("(\\s)+"), " ")
        textViewDetailPrice.text = price
        textViewDetailVariation.text = changeVariation

        textViewDetailDescription.text = product.description
    }

    override fun onDestroyView() {
        view?.viewTreeObserver?.addOnGlobalLayoutListener(null)
        super.onDestroyView()
    }

   /* override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view!!.parent.parent.parent as View).fitsSystemWindows = false
    }*/

}