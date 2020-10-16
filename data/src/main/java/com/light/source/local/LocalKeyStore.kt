package com.light.source.local

import com.light.domain.model.Bearer


interface LocalKeyStore {
    fun saveBearerToken(credentials: Bearer)
    fun loadBearerToken(): Bearer?
    fun removeToken()
}