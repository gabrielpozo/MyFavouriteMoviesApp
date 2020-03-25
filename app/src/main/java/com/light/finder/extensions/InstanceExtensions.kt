package com.light.finder.extensions

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.finder.ui.BaseFragment
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.expert.ExpertFragment
import com.light.finder.ui.lightfinder.CategoriesFragment
import com.light.finder.ui.lightfinder.DetailFragment
import com.light.finder.ui.lightfinder.ProductOptionsFragment
import com.light.finder.ui.lightfinder.ProductsFragment

fun CameraFragment.Companion.newInstance(): CameraFragment = CameraFragment()

fun CartFragment.Companion.newInstance(): CartFragment = CartFragment()

fun ExpertFragment.Companion.newInstance(): ExpertFragment = ExpertFragment()

fun ProductsFragment.Companion.newInstance(category: Category): ProductsFragment {
    val args = android.os.Bundle()
    args.putParcelable(PRODUCTS_ID_KEY, category.parcelizeCategory())
    val fragment = ProductsFragment()
    fragment.arguments = args
    return fragment
}


fun CategoriesFragment.Companion.newInstance(message: Message): CategoriesFragment {
    val args = android.os.Bundle()
    args.putParcelable(CATEGORIES_ID_KEY, message.parcelizeMessage())
    val fragment = CategoriesFragment()
    fragment.arguments = args
    return fragment
}

fun DetailFragment.Companion.newInstance(category: Category): DetailFragment {
    val args = android.os.Bundle()
    args.putParcelable(PRODUCTS_ID_KEY, category.parcelizeCategory())
    val fragment = DetailFragment()
    fragment.arguments = args
    return fragment
}


fun ProductOptionsFragment.Companion.newInstance(productList: List<Product>, originFragment: DetailFragment): ProductOptionsFragment {
    val args = android.os.Bundle()
    args.putParcelableArrayList(PRODUCTS_OPTIONS_ID_KEY, productList.parcelizeProductList())
    val fragment = ProductOptionsFragment()
    fragment.setTargetFragment(originFragment,REQUEST_CODE_PRODUCT)
    fragment.arguments = args
    return fragment
}



inline fun <reified T : Fragment> Context.intentFor(body: Intent.() -> Unit): Intent =
    Intent(this, T::class.java).apply(body)


inline fun <reified T : Fragment> BaseFragment.initializeIntent(body: Intent.() -> Unit): Intent =
    Intent(activity, T::class.java).apply(body)


/*fun ProductDetailBottomSheet.Companion.newInstance(product: Product): ProductDetailBottomSheet {
    val args = android.os.Bundle()
    args.putParcelable(PRODUCT_DETAIL_ID_KEY, mapDomainProductToParcelable(product))
    val fragment = ProductDetailBottomSheet()
    fragment.arguments = args
    return fragment
}*/

//fun CategoriesFragment.Companion.newInstance(base64: String): CategoriesFragment {
//    val args = android.os.Bundle()
//    args.putString(CATEGORIES_ID_KEY, base64)
//    val fragment = CategoriesFragment()
//    fragment.arguments = args
//    return fragment
//}



