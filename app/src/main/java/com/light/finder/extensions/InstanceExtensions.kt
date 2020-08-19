package com.light.finder.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.light.domain.model.Category
import com.light.domain.model.FormFactorTypeBaseId
import com.light.domain.model.Message
import com.light.domain.model.ShapeBrowsing
import com.light.finder.ui.about.AboutFragment
import com.light.finder.ui.browse.BrowseChoiceFragment
import com.light.finder.ui.browse.BrowseResultFragment
import com.light.finder.ui.browse.BrowseShapeFragment
import com.light.finder.ui.camera.CameraFragment
import com.light.finder.ui.cart.CartFragment
import com.light.finder.ui.lightfinder.CategoriesFragment
import com.light.finder.ui.lightfinder.DetailFragment
import com.light.finder.ui.lightfinder.ProductVariationsLightFinderActivity
import com.light.finder.ui.lightfinder.TipsAndTricksLightFinderActivity


inline fun <reified T : Activity> Context.intentFor(body: Intent.() -> Unit): Intent =
    Intent(this, T::class.java).apply(body)

inline fun <reified T : Activity> Context.startActivity(body: Intent.() -> Unit) {
    startActivity(intentFor<T>(body))
}

inline fun <reified T : Activity> AppCompatActivity.startActivityForResult(
    codeRequest: Int = 0,
    body: Intent.() -> Unit
) {
    startActivityForResult(intentFor<T>(body), codeRequest)
}


inline fun AppCompatActivity.setIntentForResult(body: Intent.() -> Unit) {
    intent.apply(body)
    setResult(Activity.RESULT_OK, intent)
}

fun CameraFragment.Companion.newInstance(): CameraFragment = CameraFragment()

fun CartFragment.Companion.newInstance(): CartFragment = CartFragment()

fun AboutFragment.Companion.newInstance(): AboutFragment = AboutFragment()

fun BrowseResultFragment.Companion.newInstance(shapeBrowsingProducts: List<ShapeBrowsing>): BrowseResultFragment {
    val args = android.os.Bundle()
    args.putParcelableArrayList(
        BrowseResultFragment.CATEGORIES_BROWSE_ID_KEY,
        shapeBrowsingProducts.parcelizeBrowsingList()
    )
    val fragment = BrowseResultFragment()
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


fun BrowseShapeFragment.Companion.newInstance(formFactorTypeBase: FormFactorTypeBaseId): BrowseShapeFragment {
    val args = android.os.Bundle()
    args.putParcelable(SHAPE_ID_KEY, formFactorTypeBase.parcelizeFormFactor())
    val fragment = BrowseShapeFragment()
    fragment.arguments = args
    return  fragment
}

//todo parcelize list
fun BrowseChoiceFragment.Companion.newInstance(productsShapeSelected: List<ShapeBrowsing>): BrowseChoiceFragment {
    val args = android.os.Bundle()
    args.putParcelableArrayList(CHOICE_ID_KEY, productsShapeSelected)
    val fragment = BrowseChoiceFragment()
    fragment.arguments = args
    return  fragment
}

fun DetailFragment.Companion.newInstance(category: Category): DetailFragment {
    val args = android.os.Bundle()
    args.putParcelable(PRODUCTS_ID_KEY, category.parcelizeCategory())
    val fragment = DetailFragment()
    fragment.arguments = args
    return fragment
}


fun TipsAndTricksLightFinderActivity.Companion.newInstance(): TipsAndTricksLightFinderActivity =
    TipsAndTricksLightFinderActivity()


//inline fun <reified T : Fragment> BaseFragment.initializeIntent(body: Intent.() -> Unit): Intent =
inline fun <reified T : Fragment> ProductVariationsLightFinderActivity.initializeIntent(body: Intent.() -> Unit): Intent =
    Intent(this, T::class.java).apply(body)

