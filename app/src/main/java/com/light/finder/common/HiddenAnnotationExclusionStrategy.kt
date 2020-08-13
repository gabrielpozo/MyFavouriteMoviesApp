package com.light.finder.common

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.light.finder.data.source.remote.dto.EnergySaving
import com.light.finder.data.source.remote.dto.Price
import timber.log.Timber

class HiddenAnnotationExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }

    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        if (f?.name == "categoryEnergySave" && f?.declaredClass.isInstance(EnergySaving::class)) {
            Timber.d("WE ARE SKIPPING THIS FIELD")
            return true
        }
        if (f?.name == "categoryPrice" && f?.declaredClass.isInstance(Price::class)) {
            Timber.d("WE ARE SKIPPING THIS FIELD")
            return true
        }
        return false

    }

}