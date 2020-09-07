package com.light.finder.data.source.utils

import com.light.finder.data.source.remote.services.OAuthRemoteUtil
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = when {
        response.retryCount > 2 -> null
        else -> runBlocking {
            response.createSignedRequest()
        }
    }

    private suspend fun Response.createSignedRequest(): Request? = try {
        // get a new token
        val tokenService = OAuthRemoteUtil.service.fetchBearerTokenAsync()
        val accessToken = tokenService.body()?.accessToken

        accessToken?.let { request.signWithToken(it) }
    } catch (error: Throwable) {
        null
    }
}

private val Response.retryCount: Int
    get() {
        var currentResponse = priorResponse
        var result = 0
        while (currentResponse != null) {
            result++
            currentResponse = currentResponse.priorResponse
        }
        return result
    }

fun Request.signWithToken(accessToken: String) =
    newBuilder()
        .header("Authorization", "Bearer $accessToken")
        .build()