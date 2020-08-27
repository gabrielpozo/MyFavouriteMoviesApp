package com.light.domain

import com.light.domain.model.Key
import com.light.domain.model.ProductBrowsing

fun ProductBrowsing.toKey() = Key(productCategoryCode, productFormfactorShapeId)
