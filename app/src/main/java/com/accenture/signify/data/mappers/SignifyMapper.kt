package com.accenture.signify.data.mappers

import com.accenture.domain.model.Categories
import com.accenture.domain.model.Message
import com.accenture.signify.data.source.remote.MessageDto

val mapServerMessagesToDomain: (List<MessageDto>) -> List<Message> = { messagesListDto ->
    val messageList: ArrayList<Message> = ArrayList()
    messagesListDto.map { messageDto ->
        val categoriesList: ArrayList<Categories> = ArrayList()
        messageDto.categories.map {categoryDto ->
            val myCategories = Categories(
                categoryProducts = categoryDto.categoryProducts,
                categoryEnergySave = categoryDto.categoryEnergysave,
                categoryIndex = categoryDto.categoryIndex,
                categoryImage = categoryDto.categoryImage,
                categoryName = categoryDto.categoryName,
                categoryPrice = categoryDto.categoryPrice
            )
            categoriesList.add(myCategories)
        }
        val message = Message(
                categories = categoriesList,
                version = messageDto.version,
                formFactor = messageDto.formFactor
            )
        messageList.add(message)
    }

    messageList
}