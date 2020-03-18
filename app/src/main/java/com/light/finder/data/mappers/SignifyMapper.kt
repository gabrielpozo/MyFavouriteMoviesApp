package com.light.finder.data.mappers

import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.finder.data.source.remote.MessageDto
import com.light.finder.data.source.remote.ProductDto

val mapServerMessagesToDomain: (MessageDto) -> Message = { messageDto ->

    val categoriesList: ArrayList<Category> = ArrayList()
    messageDto.categories?.map { categoryDto ->
        if (categoryDto.categoryProducts?.isNotEmpty() == true) {
            categoriesList.add(
                Category(
                    categoryProductBase = categoryDto.categoryProductBase ?: "",
                    categoryProducts = categoryDto.categoryProducts?.map(mapServerProductToDomain),
                    categoryName = categoryDto.categoryName ?: "",
                    categoryIndex = categoryDto.categoryIndex ?: 0,
                    categoryImage = categoryDto.categoryImage ?: "",
                    priceRange = getMinMaxPriceTag(
                        categoryDto.categoryPrice?.minPrice,
                        categoryDto.categoryPrice?.maxPrice
                    ),
                    minWattage = categoryDto.categoryWattReplace?.let { list ->
                        if (list.isNotEmpty()) {
                            list[0].toString()
                        } else ""
                    } ?: "",
                    maxWattage = categoryDto.categoryWattReplace?.let { list ->
                        if (list.isNotEmpty()) {
                            list[1].toString()
                        } else ""
                    } ?: "",
                    colors = categoryDto.categoryCctCode?.map { code ->
                        when (code) {
                            1 -> "Warm"
                            2 -> "Warm white"
                            3 -> "Cool white"
                            4 -> "Daylight"
                            else -> ""
                        }
                    } ?: emptyList()
                )
            )
        }
    }

    Message(
        categories = categoriesList
    )
}

private val mapServerProductToDomain: (ProductDto) -> Product = { productDto ->
    Product(
        name = productDto.name ?: "",
        index = productDto.index ?: 0,
        spec1 = productDto.spec1 ?: 0.0f,
        spec3 = productDto.spec3 ?: emptyList(),
        spec2 = productDto.spec2 ?: "",
        imageUrls = productDto.imageUrls ?: emptyList(),
        description = productDto.description ?: "",
        scene = productDto.scene ?: "",
        categoryName = productDto.categoryName ?: "",
        sapID12NC = productDto.sapID12NC ?: 0,
        qtyLampscase = productDto.qtyLampscase ?: 0,
        wattageReplaced = productDto.wattageReplaced ?: 0,
        country = productDto.country ?: "",
        priority = productDto.priority ?: 0,
        wattageClaim = productDto.wattageClaim ?: 0.0f,
        factorBase = productDto.factorBase ?: "",
        discountProc = productDto.discountProc ?: 0,
        sapID10NC = productDto.sapID10NC ?: 0,
        dimmingCode = productDto.dimmingCode ?: 0,
        finish = productDto.finish ?: "",
        promoted = productDto.promoted ?: 0,
        priceSku = productDto.priceSku ?: 0.0f,
        priceLamp = productDto.priceLamp ?: 0.0f,
        pricePack = productDto.pricePack ?: 0.0f,
        factorShape = productDto.factorShape ?: "",
        qtyLampSku = productDto.qtyLampSku ?: 0,
        discountValue = productDto.discountValue ?: 0,
        qtySkuCase = productDto.qtySkuCase ?: 0,
        factorTypeCode = productDto.factorTypeCode ?: 0,
        colorCctCode = when (productDto.productCctCode) {
            1 -> "Warm"
            2 -> "Warm white"
            3 -> "Cool white"
            4 -> "Daylight"
            else -> ""

        }
    )
}


fun getMinMaxPriceTag(minPrice: Float?, maxPrice: Float?): String =
    if (minPrice == null || maxPrice == null) {
        "-"

    } else if (minPrice == maxPrice && minPrice != 0.0f) {
        minPrice.toString()

    } else {
        "$$minPrice-$$maxPrice"
    }