package com.accenture.signify.data.mappers

import com.accenture.domain.model.Categories
import com.accenture.domain.model.Message
import com.accenture.signify.data.source.remote.MessageDto

val mapServerMessagesToDomain: (MessageDto) -> Message = { messageDto ->
    val categoriesList: ArrayList<Categories> = ArrayList()
    messageDto.categories.map { categoryDto ->
        val category = Categories(
            categoryProducts = categoryDto.categoryProducts,
            categoryEnergySave = categoryDto.categoryEnergysave,
            categoryIndex = categoryDto.categoryIndex,
            categoryImage = categoryDto.categoryImage,
            categoryName = categoryDto.categoryName,
            categoryPrice = categoryDto.categoryPrice
        )
        categoriesList.add(category)
    }
    val message = Message(
        categories = categoriesList,
        version = messageDto.version,
        formFactor = messageDto.formFactor
    )

    message

}