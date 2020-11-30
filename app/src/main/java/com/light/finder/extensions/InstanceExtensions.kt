package com.light.finder.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.light.domain.model.*
import com.light.finder.ui.about.AboutFragment
import com.light.finder.ui.browse.BrowseChoiceFragment
import com.light.finder.ui.browse.BrowseFittingFragment
import com.light.finder.ui.browse.BrowseFittingFragment.Companion.FITTING_BACK_CODE
import com.light.finder.ui.browse.BrowseResultFragment
import com.light.finder.ui.browse.BrowseShapeFragment
import com.light.finder.ui.browse.BrowseShapeFragment.Companion.SHAPE_BACK_CODE
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

fun Context.startBrowsingActivity(url: String, browserNotFound: () -> Unit = {}) {
    val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (browseIntent.resolveActivity(packageManager) != null) {
        startActivity(browseIntent)
    } else {
        browserNotFound.invoke()
    }
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


fun Fragment.setTargetScreenForResult(key: String, code: Int) {
    setTargetFragment({
        putExtra(key, true)
    }, code)
}

fun Fragment.setTargetFragment(body: Intent.() -> Unit, code: Int) {
    val intent = Intent()
    intent.apply(body)
    targetFragment?.onActivityResult(code, Activity.RESULT_OK, intent)
}

fun CameraFragment.Companion.newInstance(): CameraFragment = CameraFragment()

fun CameraFragment.Companion.newInstance(exitOnBack: Boolean = false): CameraFragment {
    val args = android.os.Bundle()
    args.putBoolean("EXIT", exitOnBack)
    val fragment = CameraFragment()
    fragment.arguments = args
    return fragment
}

fun CartFragment.Companion.newInstance(): CartFragment = CartFragment()

fun AboutFragment.Companion.newInstance(): AboutFragment = AboutFragment()

fun BrowseResultFragment.Companion.newInstance(
    choiceBrowsingProducts: List<ChoiceBrowsing>,
    shapeBrowsingProducts: ArrayList<ShapeBrowsing>?,
    formFactorId: Int?,
    formFactorName: String?
): BrowseResultFragment {
    val args = android.os.Bundle()
    args.putParcelableArrayList(
        CATEGORIES_BROWSE_CHOICE_ID_KEY,
        choiceBrowsingProducts.parcelizeChoiceBrowsingList()
    )

    args.putParcelableArrayList(
        CATEGORIES_BROWSE_SHAPE_ID_KEY,
        shapeBrowsingProducts?.parcelizeBrowsingList()
    )

    args.putInt(CATEGORIES_FORM_FACTOR_SHAPE_ID_KEY, formFactorId ?: -1)
    args.putString(CATEGORIES_FORM_FACTOR_SHAPE_NAME_KEY, formFactorName)

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


fun BrowseShapeFragment.Companion.newInstance(
    fittingFragment: BrowseFittingFragment,
    formFactorTypeBase: FormFactorTypeBaseId
): BrowseShapeFragment {
    val args = android.os.Bundle()
    args.putParcelable(SHAPE_ID_KEY, formFactorTypeBase.parcelizeFormFactor())
    val fragment = BrowseShapeFragment()
    fragment.arguments = args
    fragment.setTargetFragment(fittingFragment, FITTING_BACK_CODE)
    return fragment
}

fun BrowseShapeFragment.Companion.newInstanceForShapeChoiceEditBrowse(): BrowseShapeFragment {
    val args = android.os.Bundle()
    args.putInt(
        SHAPE_EDIT_ID_KEY,
        SHAPE_NUMBER_KEY
    )
    val fragment = BrowseShapeFragment()
    fragment.arguments = args
    return fragment
}

fun BrowseFittingFragment.Companion.newInstanceForFittingEditBrowse(): BrowseFittingFragment {
    val args = android.os.Bundle()
    args.putInt(
        FITTING_EDIT_ID_KEY,
        1
    )
    val fragment = BrowseFittingFragment()
    fragment.arguments = args
    return fragment
}


//todo parcelize list
fun BrowseChoiceFragment.Companion.newInstance(
    browseShapeFragment: BrowseShapeFragment,
    productsShapeSelected: List<ShapeBrowsing>,
    formFactorTypeBaseId: Int,
    formFactorName: String?
): BrowseChoiceFragment {
    val args = android.os.Bundle()
    args.putParcelableArrayList(CHOICE_ID_KEY, productsShapeSelected.parcelizeBrowsingList())
    args.putInt(CHOICE_FITTING_ID, formFactorTypeBaseId)
    args.putString(CHOICE_FITTING_NAME, formFactorName)
    val fragment = BrowseChoiceFragment()
    fragment.arguments = args
    fragment.setTargetFragment(browseShapeFragment, SHAPE_BACK_CODE)
    return fragment
}

//todo parcelize list
fun BrowseChoiceFragment.Companion.newInstanceForShapeChoiceEditBrowse(): BrowseChoiceFragment {
    val args = android.os.Bundle()
    args.putInt(
        CHOICE_EDIT_ID_KEY,
        CHOICE_NUMBER_KEY
    )
    val fragment = BrowseChoiceFragment()
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


fun TipsAndTricksLightFinderActivity.Companion.newInstance(): TipsAndTricksLightFinderActivity =
    TipsAndTricksLightFinderActivity()


//inline fun <reified T : Fragment> BaseFragment.initializeIntent(body: Intent.() -> Unit): Intent =
inline fun <reified T : Fragment> ProductVariationsLightFinderActivity.initializeIntent(body: Intent.() -> Unit): Intent =
    Intent(this, T::class.java).apply(body)

