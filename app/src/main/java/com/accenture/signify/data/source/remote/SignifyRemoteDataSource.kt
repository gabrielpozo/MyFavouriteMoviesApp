package com.accenture.signify.data.source.remote

import com.accenture.data.Result
import com.accenture.domain.model.Message
import com.accenture.source.BaseDataSource
import com.accenture.source.RemoteDataSource

class SignifyRemoteDataSource constructor(private val signifyApiService: SignifyApiService) :
    BaseDataSource(), RemoteDataSource {
    override suspend fun fetchMessages(): Result<List<Message>> {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}
