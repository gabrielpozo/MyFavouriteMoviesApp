package com.accenture.signify.data.mappers

import com.accenture.domain.model.Categories
import com.accenture.domain.model.Message
import com.accenture.domain.model.Product
import com.accenture.signify.data.source.remote.MessageDto

val mapServerMessagesToDomain: (MessageDto) -> Message = { messageDto ->

    val categoriesList: ArrayList<Categories> = ArrayList()
    messageDto.categories.map { categoryDto ->
        val products: ArrayList<Product> = ArrayList()
        categoryDto.categoryProducts.map { productDto ->
            products.add(
                Product(
                    productDto.productImage,
                    productDto.productName,
                    productDto.productDescription,
                    productDto.productSpecOne,
                    productDto.productSpecThree,
                    productDto.productScene
                )
            )
        }
        categoriesList.add(
            Categories(
                categoryProducts = products,
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
