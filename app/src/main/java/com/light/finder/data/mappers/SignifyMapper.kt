package com.light.finder.data.mappers

import com.light.domain.model.Category
import com.light.domain.model.Message
import com.light.domain.model.Product
import com.light.finder.data.source.remote.MessageDto
import com.light.finder.data.source.remote.ProductDto

val mapServerMessagesToDomain: (MessageDto) -> Message = { messageDto ->

    val categoriesList: ArrayList<Category> = ArrayList()
    messageDto.categories?.map { categoryDto ->
        categoriesList.add(
            Category(
                categoryProducts = categoryDto.categoryProducts?.map(mapServerProductToDomain)
                    ?: emptyList(),
                categoryName = categoryDto.categoryName ?: "",
                categoryIndex = categoryDto.categoryIndex ?: 0,
                categoryImage = categoryDto.categoryImage ?: ""

            )
        )
    }

    Message(
        categories = categoriesList
    )
}

private val mapServerProductToDomain: (ProductDto) -> Product = { productDto ->
    Product(
        productImage = productDto.productImage ?: emptyList(),
        productName = productDto.productName ?: "",
        productDescription = productDto.productDescription ?: "",
        productSpecOne = productDto.productSpecOne ?: "",
        productSpecThree = productDto.productSpecThree ?: "",
        productScene = productDto.productScene ?: ""
    )
}
