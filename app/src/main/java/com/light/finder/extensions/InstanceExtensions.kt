package com.light.finder.extensions

import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.expert.ExpertFragment
import com.light.finder.ui.lightfinder.CategoriesFragment
import com.light.finder.ui.lightfinder.DetailFragment
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

fun DetailFragment.Companion.newInstance(category: Category): DetailFragment {
    val args = android.os.Bundle()
    args.putParcelable(PRODUCTS_ID_KEY, category.parcelizeCategory())
    val fragment = DetailFragment()
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

//fun CategoriesFragment.Companion.newInstance(base64: String): CategoriesFragment {
//    val args = android.os.Bundle()
//    args.putString(CATEGORIES_ID_KEY, base64)
//    val fragment = CategoriesFragment()
//    fragment.arguments = args
//    return fragment
//}



