package com.light.finder.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.finder.R
import com.light.finder.SignifyApp
import com.light.finder.data.source.remote.CategoryParcelable
import com.light.finder.data.source.remote.MessageParcelable
import com.light.finder.data.source.remote.ProductParcelable
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
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) =
    Delegates.observable(initialValue) { _, old, new ->
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(old[oldItemPosition], new[newItemPosition])

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areContentsTheSame(old[oldItemPosition], new[newItemPosition])

            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = new.size
        }).dispatchUpdatesTo(this@basicDiffUtil)
    }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)


fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url).placeholder(R.drawable.category_placeholder).into(this)
}

fun ImageView.loadUrlCenterCrop(url: String) {
    Glide.with(context).load(url).centerInside().placeholder(R.drawable.category_placeholder).into(this)
}


fun Category.parcelizeCategory(): CategoryParcelable =
    CategoryParcelable(
        categoryProductBase,
        categoryProducts.map(mapDomainProductToParcelable),
        categoryIndex,
        categoryName,
        categoryImage,
        priceRange,
        minWattage,
        maxWattage,
        colors
    )


fun CategoryParcelable.deparcelizeCategory(): Category =
    Category(
        categoryProductBase,
        categoryProducts.map(mapParcelableProductToDomain),
        categoryIndex,
        categoryName,
        categoryImage,
        priceRange,
        minWattage,
        maxWattage,
        colors
    )


fun Message.parcelizeMessage(): MessageParcelable =
    MessageParcelable(categories.map { it.parcelizeCategory() })


fun MessageParcelable.deparcelizeMessage(): Message =
    Message(this.categoriesList.map { it.deparcelizeCategory() })


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
        product.formfactorType
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
        product.formfactorType
    )

}

