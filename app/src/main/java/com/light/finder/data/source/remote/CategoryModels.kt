package com.light.finder.data.source.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryParcelable(
    val categoryProducts: List<ProductParcelable>,
    val categoryIndex: Int,
    val categoryName: String,
    val categoryImage: String
) : Parcelable

@Parcelize
data class ProductParcelable(
    val productImage: List<String>,
    val productName: String,
    val productDescription: String,
    val productSpecOne: String,
    val productSpecThree: String,
    val productScene: String
) : Parcelable

@Parcelize
data class MessageParcelable(
    val categoriesList: List<CategoryParcelable>
) : Parcelable