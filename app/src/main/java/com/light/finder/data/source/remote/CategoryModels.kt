package com.light.finder.data.source.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryParcelable(
    val categoryProductBase: String,
    val categoryProducts: List<ProductParcelable>,
    val categoryIndex: Int,
    val categoryName: String,
    val categoryImage: String,
    val priceRange: String,
    val minWattage: String,
    val maxWattage: String,
    val colors: List<String>
) : Parcelable

@Parcelize
data class ProductParcelable(
    val productImage: List<String>,
    val productCategoryName: String,
    val productName: String,
    val productDescription: String,
    val productSpecOne: Float,
    val productSpecThree: List<String>,
    val productScene: String,
    val productPrice: Float
) : Parcelable

@Parcelize
data class MessageParcelable(
    val categoriesList: List<CategoryParcelable>
) : Parcelable

