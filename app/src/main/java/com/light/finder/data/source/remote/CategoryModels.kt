package com.light.finder.data.source.remote

import android.os.Parcelable
import com.light.domain.model.ShapeBrowsing
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryParcelable(
    val categoryProductBase: String,
    val categoryProducts: List<ProductParcelable>,
    val categoryIndex: Int,
    val categoryName: String,
    val categoryImage: String,
    val priceRange: String,
    val minWattage: List<Int>,
    val maxEnergySaving: Float,
    val minEnergySaving: Float,
    val colors: List<Int>,
    val finishCodes: List<Int>,
    val categoryShape: String
) : Parcelable

@Parcelize
data class ProductParcelable(
    var name: String,
    var index: Int,
    var spec1: Float,
    var spec3: List<Int>,
    var spec2: String,
    var imageUrls: List<String>,
    var description: String,
    var scene: String,
    var categoryName: String,
    var sapID12NC: Long,
    var qtyLampscase: Int,
    var wattageReplaced: Int,
    var country: String,
    var priority: Int,
    var wattageClaim: Float,
    var factorBase: String,
    var discountProc: Int,
    var sapID10NC: Long,
    var dimmingCode: Int,
    var finish: String,
    var promoted: Int,
    var priceSku: Float,
    var priceLamp: Float,
    var pricePack: Float,
    var factorShape: String,
    var qtyLampSku: Int,
    var discountValue: Int,
    var qtySkuCase: Int,
    var factorTypeCode: Int,
    var colorCctCode: Int,
    var formfactorType: Int,
    var productFinishCode: Int,
    var productCategoryCode: Int,
    var isSelected: Boolean,
    var isAvailable: Boolean,
    var wattageReplacedExtra: String
) : Parcelable

@Parcelize
data class MessageParcelable(
    val categoriesList: List<CategoryParcelable>,
    val version: String,
    val baseIdentified: String,
    val formfactorType: String,
    val shapeIdentified: String
) : Parcelable

@Parcelize
data class CctTypeParcelable(
    val id: Int,
    val name: String,
    val smallIcon: String,
    val bigIcon: String,
    val order: Int,
    val arType: Int,
    val kelvinSpec: KelvinSpecParcelable,
    var isSelected: Boolean = false
) : Parcelable


@Parcelize
data class KelvinSpecParcelable(val minValue: Int, val maxValue: Int, val defaultValue: Int) :
    Parcelable


@Parcelize
data class ShapeBrowsingParcelable(
    val id: Int,
    val name: String,
    val image: String?,
    val order: Int,
    val subtitleCount:Int,
    var isSelected: Boolean = false
) : Parcelable

