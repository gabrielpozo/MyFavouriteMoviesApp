package com.light.finder.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.light.domain.model.Key
import com.light.domain.model.ProductBrowsing


inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)