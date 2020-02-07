package com.accenture.signify.data.source.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryParcelable(
    val categoryProducts: List<ProductParcelable>,
    val categoryEnergySave: String,
    val categoryIndex: Int,
    val categoryName: String,
    val categoryImage: String,
    val categoryPrice: String
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