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
        productImage = productDto.productImage ?: emptyList(),
        productCategoryName = productDto.productCategoryName ?: "",
        productName = productDto.productName ?: "",
        productDescription = productDto.productDescription ?: "",
        productSpecOne = productDto.productSpecOne ?: 0.0f,
        productSpecThree = productDto.productSpecThree ?: emptyList(),
        productScene = productDto.productScene ?: "",
        productPrice = productDto.productPrice ?: 0.0f
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