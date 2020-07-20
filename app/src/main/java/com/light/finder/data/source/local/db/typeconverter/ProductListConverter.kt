package com.light.finder.data.source.local.db.typeconverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.light.domain.model.ProductBrowsing
import java.lang.reflect.Type

class ProductListConverter {
    @androidx.room.TypeConverter
    fun fromProductBrowsingList(productList: List<ProductBrowsing?>?): String? {
        if (productList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<ProductBrowsing?>?>() {}.type
        return gson.toJson(productList, type)
    }

    @androidx.room.TypeConverter
    fun toProductBrowsingList(productListString: String?): List<ProductBrowsing?>? {
        if (productListString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<ProductBrowsing?>?>() {}.type
        return gson.fromJson(productListString, type)
    }
}