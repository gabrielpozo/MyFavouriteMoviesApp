package com.light.finder.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.light.domain.model.*
import com.light.finder.R
import com.light.finder.SignifyApp
import com.light.finder.data.source.remote.*
import kotlin.properties.Delegates


@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {

    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}


@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {

    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}

val Context.app: SignifyApp
    get() = applicationContext as SignifyApp


inline fun <VH : RecyclerView.ViewHolder, T> RecyclerView.Adapter<VH>.basicDiffUtil(
    initialValue: List<T>,
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    shouldRefreshData: Boolean = true
) =
    Delegates.observable(initialValue) { _, old, new ->
        var count = 0
        val diffUtil = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(old[oldItemPosition], new[newItemPosition])

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areContentsTheSame(old[oldItemPosition], new[newItemPosition])

            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = new.size
        })

        if (count == 1 && !shouldRefreshData) {
            return@observable
        }

        count += 1
        diffUtil.dispatchUpdatesTo(this@basicDiffUtil)

    }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)


fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url)
        .override(460, 460)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.category_placeholder).into(this)
}

fun ImageView.loadIdentified(url: String) {
    Glide.with(context).load(url).into(this)
}

fun ImageView.loadUrWithoutPlaceholderl(url: String) {
    Glide.with(context).load(url)
        .override(460, 460)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadFitting(url: String) {
    Glide.with(context).load(url)
        .placeholder(R.color.backgroundLight).into(this)
}

fun ImageView.loadThumbnail(url: String) {
    Glide.with(context)
        .load(url)
        .circleCrop()
        .placeholder(R.drawable.ic_holder)
        .into(this)
}

fun ImageView.loadThumbnailVariation(url: String) {
    Glide.with(context)
        .load(url)
        .circleCrop()
        .placeholder(R.drawable.ic_placeholder_variation)
        .into(this)
}


fun ImageView.loadBulbThumbnail(url: String) {
    Glide.with(context)
        .load(url)
        .circleCrop()
        .centerInside()
        .placeholder(R.drawable.all_circle).into(this)

}

fun ImageView.loadUrlCenterCrop(url: String) {
    Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).centerInside()
        .placeholder(R.drawable.fallback_image)
        .into(this)
}

fun ImageView.loadCircleImage(url: String) {
    Glide.with(context).load(url).circleCrop().placeholder(R.drawable.ic_placeholder_variation)
        .into(this)
}


fun ImageView.setPlaceholder() {
    Glide.with(context).load(R.drawable.fallback_image).centerInside()
        .into(this)
}

fun TextView.loadSmallColorIcon(url: String, @DrawableRes id: Int = 0) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_holder)
        .error(R.drawable.ic_holder)
        .into(object : CustomTarget<Drawable>(75, 75) {
            override fun onLoadCleared(drawable: Drawable?) {
                setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }

            override fun onResourceReady(
                res: Drawable,
                transition: com.bumptech.glide.request.transition.Transition<in Drawable>?
            ) {
                setCompoundDrawablesWithIntrinsicBounds(null, null, res, null)
            }
        })
}

fun Category.parcelizeCategory(): CategoryParcelable =
    CategoryParcelable(
        categoryProductBase,
        categoryProducts.map(mapDomainProductToParcelable),
        categoryIndex,
        categoryName,
        categoryImage,
        priceRange,
        maxPrice,
        minPrice,
        categoryWattReplaced,
        maxEnergySaving,
        minEnergySaving,
        colors,
        finishCodes,
        categoryShape,
        categoryConnectivityCode,
        categoryDescription
    )


fun CategoryParcelable.deparcelizeCategory(): Category =
    Category(
        categoryProductBase,
        categoryProducts.map(mapParcelableProductToDomain),
        categoryIndex,
        categoryName,
        categoryImage,
        priceRange,
        maxPrice,
        minPrice,
        minWattage,
        maxEnergySaving,
        minEnergySaving,
        colors,
        finishCodes,
        categoryShape,
        categoryConnectivityCode,
        categoryDescription
    )


fun List<Product>.parcelizeProductList(): ArrayList<ProductParcelable> {
    val parcelizeProducts = ArrayList<ProductParcelable>()
    forEach { product ->
        parcelizeProducts.add(mapDomainProductToParcelable(product))
    }
    return parcelizeProducts
}


fun List<CctType>.parcelizeCctList(): ArrayList<CctTypeParcelable> {
    val parcelizeCctType = ArrayList<CctTypeParcelable>()
    forEach { cctColor ->
        parcelizeCctType.add(mapDomainCctToParcelable(cctColor))
    }
    return parcelizeCctType
}

fun List<CctTypeParcelable>.deparcelizeCctList(): ArrayList<CctType> {
    val parcelizeCct = ArrayList<CctType>()
    forEach { cctParcelable ->
        parcelizeCct.add(mapParcelableCctToDomain(cctParcelable))
    }
    return parcelizeCct
}


fun List<ShapeBrowsing>.parcelizeBrowsingList(): ArrayList<ShapeBrowsingParcelable> {
    val parcelizeBrowsing = ArrayList<ShapeBrowsingParcelable>()
    forEach { cctColor ->
        parcelizeBrowsing.add(mapDomainShapeBrowsingToParcelable(cctColor))
    }
    return parcelizeBrowsing
}


fun List<ShapeBrowsingParcelable>.deParcelizeBrowsingList(): ArrayList<ShapeBrowsing> {
    val parcelizeBrowsing = ArrayList<ShapeBrowsing>()
    forEach { shapeParcelable ->
        parcelizeBrowsing.add(mapParcelizeShapeBrowsingToDomain(shapeParcelable))
    }
    return parcelizeBrowsing
}


fun List<ChoiceBrowsing>.parcelizeChoiceBrowsingList(): ArrayList<ChoiceBrowsingParcelable> {
    val parcelizeChoiceBrowsing = ArrayList<ChoiceBrowsingParcelable>()
    forEach { categoryChoice ->
        parcelizeChoiceBrowsing.add(mapDomainChoiceBrowsingToParcelable(categoryChoice))
    }
    return parcelizeChoiceBrowsing
}

fun List<ChoiceBrowsingParcelable>.deparcelizeChoiceBrowsingList(): ArrayList<ChoiceBrowsing> {
    val parcelizeChoiceBrowsing = ArrayList<ChoiceBrowsing>()
    forEach { categoryChoice ->
        parcelizeChoiceBrowsing.add(mapDomainChoiceBrowsingToDomain(categoryChoice))
    }
    return parcelizeChoiceBrowsing
}


fun List<ProductParcelable>.deparcelizeProductList(): ArrayList<Product> {
    val parcelizeProducts = ArrayList<Product>()
    forEach { productParcelable ->
        parcelizeProducts.add(mapParcelableProductToDomain(productParcelable))
    }
    return parcelizeProducts
}

fun Message.parcelizeMessage(): MessageParcelable =
    MessageParcelable(
        categories.map { it.parcelizeCategory() },
        version = version,
        baseIdentified = baseIdentified,
        formfactorType = formfactorType,
        shapeIdentified = shapeIdentified,
        textIdentified = textIdentified,
        imageIdentified = imageIdentified
    )

fun FormFactorTypeBaseId.parcelizeFormFactor(): FormFactorTypeBaseIdParcelable =
    FormFactorTypeBaseIdParcelable(
        id = id,
        name = name,
        image = image,
        description = description,
        order = order,
        isSelected = isSelected
    )


fun MessageParcelable.deparcelizeMessage(): Message =
    Message(
        this.categoriesList.map { it.deparcelizeCategory() },
        version = version,
        baseIdentified = baseIdentified,
        formfactorType = formfactorType,
        shapeIdentified = shapeIdentified,
        textIdentified = textIdentified,
        imageIdentified = imageIdentified
    )

fun FormFactorTypeBaseIdParcelable.deparcelizeFormFactor(): FormFactorTypeBaseId =
    FormFactorTypeBaseId(
        id = id,
        name = name,
        image = image,
        description = description,
        order = order,
        isSelected = isSelected
    )

val mapDomainProductToParcelable: (Product) -> ProductParcelable = { product ->
    ProductParcelable(
        product.name,
        product.index,
        product.spec1,
        product.spec3,
        product.spec2,
        product.imageUrls,
        product.description,
        product.scene,
        product.categoryName,
        product.sapID12NC,
        product.qtyLampscase,
        product.wattageReplaced,
        product.country,
        product.productPrio,
        product.wattageClaim,
        product.factorBase,
        product.discountProc,
        product.sapID10NC,
        product.dimmingCode,
        product.finish,
        product.promoted,
        product.priceSku,
        product.priceLamp,
        product.pricePack,
        product.factorShape,
        product.qtyLampSku,
        product.discountValue,
        product.qtySkuCase,
        product.factorTypeCode,
        product.colorCctCode,
        product.formfactorType,
        product.productFinishCode,
        product.productConnectionCode,
        product.produtCategoryCode,
        product.isSelected,
        product.isAvailable,
        product.wattageReplacedExtra,
        product.stickyHeaderFirstLine,
        product.stickyHeaderSecondLine
    )

}

val mapDomainCctToParcelable: (CctType) -> CctTypeParcelable = { cctType ->
    CctTypeParcelable(
        cctType.id,
        cctType.name,
        cctType.smallIcon,
        cctType.bigIcon,
        cctType.order,
        cctType.arType,
        mapDomainKelvinToParcelable(cctType.kelvinSpec),
        cctType.isSelected
    )

}

val mapParcelableCctToDomain: (CctTypeParcelable) -> CctType = { cctType ->
    CctType(
        cctType.id,
        cctType.name,
        cctType.smallIcon,
        cctType.bigIcon,
        cctType.order,
        cctType.arType,
        mapParcelableKelvinToDomain(cctType.kelvinSpec),
        cctType.isSelected
    )
}

val mapParcelableProductToDomain: (ProductParcelable) -> Product = { product ->
    Product(
        product.name,
        product.index,
        product.spec1,
        product.spec3,
        product.spec2,
        product.imageUrls,
        product.description,
        product.scene,
        product.categoryName,
        product.sapID12NC,
        product.qtyLampscase,
        product.wattageReplaced,
        product.country,
        product.priority,
        product.wattageClaim,
        product.factorBase,
        product.discountProc,
        product.sapID10NC,
        product.dimmingCode,
        product.finish,
        product.promoted,
        product.priceSku,
        product.priceLamp,
        product.pricePack,
        product.factorShape,
        product.qtyLampSku,
        product.discountValue,
        product.qtySkuCase,
        product.factorTypeCode,
        product.colorCctCode,
        product.formfactorType,
        product.productFinishCode,
        product.productConnectionCode,
        product.productCategoryCode,
        product.isSelected,
        product.isAvailable,
        wattageReplacedExtra = product.wattageReplacedExtra,
        stickyHeaderFirstLine = product.stickyHeaderFirstLine,
        stickyHeaderSecondLine = product.stickyHeaderSecondLine
    )

}

val mapDomainKelvinToParcelable: (KelvinSpec) -> KelvinSpecParcelable = { kelvinSpec ->
    KelvinSpecParcelable(
        kelvinSpec.minValue,
        kelvinSpec.maxValue,
        kelvinSpec.defaultValue
    )
}


val mapParcelableKelvinToDomain: (KelvinSpecParcelable) -> KelvinSpec = { kelvinSpecParcelable ->
    KelvinSpec(
        kelvinSpecParcelable.minValue,
        kelvinSpecParcelable.maxValue,
        kelvinSpecParcelable.defaultValue
    )
}


val mapDomainShapeBrowsingToParcelable: (ShapeBrowsing) -> ShapeBrowsingParcelable =
    { shapeBrowsing ->
        ShapeBrowsingParcelable(
            shapeBrowsing.id,
            shapeBrowsing.name,
            shapeBrowsing.image,
            shapeBrowsing.order,
            shapeBrowsing.subtitleCount,
            shapeBrowsing.baseIdFitting,
            shapeBrowsing.baseNameFitting,
            shapeBrowsing.isSelected
        )

    }


val mapParcelizeShapeBrowsingToDomain: (ShapeBrowsingParcelable) -> ShapeBrowsing =
    { shapeBrowsingParcelable ->
        ShapeBrowsing(
            shapeBrowsingParcelable.id,
            shapeBrowsingParcelable.name,
            shapeBrowsingParcelable.image,
            shapeBrowsingParcelable.order,
            shapeBrowsingParcelable.subtitleCount,
            shapeBrowsingParcelable.baseFittingId,
            shapeBrowsingParcelable.baseNameFitting,
            shapeBrowsingParcelable.isSelected
        )

    }

val mapDomainChoiceBrowsingToParcelable: (ChoiceBrowsing) -> ChoiceBrowsingParcelable =
    { shapeBrowsing ->
        ChoiceBrowsingParcelable(
            shapeBrowsing.id,
            shapeBrowsing.name,
            shapeBrowsing.image,
            shapeBrowsing.order,
            shapeBrowsing.description,
            shapeBrowsing.subtitleCount,
            shapeBrowsing.baseNameFitting,
            shapeBrowsing.baseIdFitting,
            shapeBrowsing.isSelected
        )

    }


val mapDomainChoiceBrowsingToDomain: (ChoiceBrowsingParcelable) -> ChoiceBrowsing =
    { choiceBrowsing ->
        ChoiceBrowsing(
            choiceBrowsing.id,
            choiceBrowsing.name,
            choiceBrowsing.order,
            choiceBrowsing.image,
            choiceBrowsing.subtitleCount,
            choiceBrowsing.description,
            choiceBrowsing.baseFittingId,
            choiceBrowsing.baseNameFitting,
            choiceBrowsing.isSelected
        )

    }


