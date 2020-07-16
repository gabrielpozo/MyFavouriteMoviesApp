package com.light.finder.data.source.local.db.typeconverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.light.finder.data.source.remote.dto.ProductDto
import java.lang.reflect.Type

class ProductListConverter {
    @androidx.room.TypeConverter
    fun fromProductList(productList: List<ProductDto?>?): String? {
        if (productList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<ProductDto?>?>() {}.type
        return gson.toJson(productList, type)
    }

    @androidx.room.TypeConverter
    fun toProductList(productListString: String?): List<ProductDto?>? {
        if (productListString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<ProductDto?>?>() {}.type
        return gson.fromJson(productListString, type)
    }
}