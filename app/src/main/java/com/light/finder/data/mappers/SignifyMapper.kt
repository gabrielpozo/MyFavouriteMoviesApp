package com.light.finder.data.mappers

import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.finder.data.source.remote.MessageDto
import com.light.finder.data.source.remote.ProductDto

val mapServerMessagesToDomain: (MessageDto) -> Message = { messageDto ->

    val categoriesList: ArrayList<Category> = ArrayList()
    messageDto.categories?.map { categoryDto ->
        val countWattages = getWattValuesCategory(categoryDto.categoryProducts)
        categoriesList.add(
            Category(
                categoryProductBase = categoryDto.categoryProductBase ?: "",
                categoryProducts = categoryDto.categoryProducts?.map(mapServerProductToDomain)
                    ?: emptyList(),
                categoryName = categoryDto.categoryName ?: "",
                categoryIndex = categoryDto.categoryIndex ?: 0,
                categoryImage = categoryDto.categoryImage ?: "",
                priceRange = getMinMaxPriceTag(
                    categoryDto.categoryPrice?.minPrice,
                    categoryDto.categoryPrice?.maxPrice
                ),
                wattageAvailable = countWattages
            )
        )
    }

    Message(
        categories = categoriesList
    )
}


fun getMinMaxPriceTag(minPrice: Float?, maxPrice: Float?): String =
    if (minPrice == null || maxPrice == null) {
        "-"

    } else if (minPrice == maxPrice) {
        minPrice.toString()

    } else {
        //TODO the format here
        "$minPrice$ - $maxPrice$"
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


fun getWattValuesCategory(categoryProducts: List<ProductDto>?): Int {
    val wattages = hashSetOf<Float>()
    categoryProducts?.map { productDto ->
        if (productDto.productSpecOne != null) {
            wattages.add(productDto.productSpecOne)
        }
    }
    return wattages.count()

}
