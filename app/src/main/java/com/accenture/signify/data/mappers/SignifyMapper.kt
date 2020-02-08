package com.accenture.signify.data.mappers

import com.accenture.domain.model.Category
import com.accenture.domain.model.Message
import com.accenture.domain.model.Product
import com.accenture.signify.data.source.remote.MessageDto
import com.accenture.signify.data.source.remote.ProductDto

val mapServerMessagesToDomain: (MessageDto) -> Message = { messageDto ->

    val categoriesList: ArrayList<Category> = ArrayList()
    messageDto.categories.map { categoryDto ->
        categoriesList.add(
            Category(
                categoryProducts = categoryDto.categoryProducts.map(mapServerProductToDomain),
                categoryEnergySave = categoryDto.categoryEnergysave,
                categoryIndex = categoryDto.categoryIndex,
                categoryImage = categoryDto.categoryImage,
                categoryName = categoryDto.categoryName,
                categoryPrice = categoryDto.categoryPrice
            )
        )
    }

    Message(
        categories = categoriesList
    )
}

private val mapServerProductToDomain: (ProductDto) -> Product = { productDto ->
    Product(
        productImage = productDto.productImage,
        productName = productDto.productName,
        productDescription = productDto.productDescription,
        productSpecOne = productDto.productSpecOne,
        productSpecThree = productDto.productSpecThree,
        productScene = productDto.productScene
    )
}
