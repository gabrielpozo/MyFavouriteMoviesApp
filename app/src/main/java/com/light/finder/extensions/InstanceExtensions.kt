package com.light.finder.extensions

import com.light.domain.model.Category
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.camera.PermissionsFragment
import com.light.finder.ui.camera.PreviewFragment
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.expert.ExpertFragment
import com.light.finder.ui.lightfinder.CategoriesFragment
import com.light.finder.ui.lightfinder.ProductsFragment

fun CameraFragment.Companion.newInstance(): CameraFragment = CameraFragment()

fun CartFragment.Companion.newInstance(): CartFragment = CartFragment()

fun ExpertFragment.Companion.newInstance(): ExpertFragment = ExpertFragment()

fun ProductsFragment.Companion.newInstance(category: Category): ProductsFragment {
    val args = android.os.Bundle()
    args.putParcelable(PRODUCTS_ID_KEY, category.parcelize())
    val fragment = ProductsFragment()
    fragment.arguments = args
    return fragment
}


fun CategoriesFragment.Companion.newInstance(base64: String): CategoriesFragment {
    val args = android.os.Bundle()
    args.putString(CATEGORIES_ID_KEY, base64)
    val fragment = CategoriesFragment()
    fragment.arguments = args
    return fragment
}

fun PreviewFragment.Companion.newInstance(absolutePath: String): PreviewFragment {
    val args = android.os.Bundle()
    args.putString(PREVIEW_ID_KEY, absolutePath)
    val fragment = PreviewFragment()
    fragment.arguments = args
    return fragment
}


