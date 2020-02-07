package com.accenture.signify.data.mappers

import com.accenture.domain.model.Categories
import com.accenture.domain.model.Message
import com.accenture.signify.data.source.remote.MessageDto

val mapServerMessagesToDomain: (MessageDto) -> Message = { messageDto ->

    val categoriesList: ArrayList<Categories> = ArrayList()
    messageDto.categories.map { categoryDto ->
        categoriesList.add(
            Categories(
                categoryName = categoryDto.categoryName
            )
        )
    }

    Message(
        categories = categoriesList
    )
}