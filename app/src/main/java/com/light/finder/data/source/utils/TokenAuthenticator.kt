package com.light.finder.data.source.utils

import com.light.domain.model.Bearer
import com.light.source.local.LocalKeyStore
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator(private val localKeyStore: LocalKeyStore) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = when {
        response.retryCount > 2 -> null
        else -> runBlocking {
            response.createSignedRequest()
        }
    }

    private suspend fun Response.createSignedRequest(): Request? = try {
        // get a new token
 /*       val tokenService = OAuthRemoteUtil.service.fetchBearerTokenAsync()
        val accessToken = tokenService.body()*/

/*        accessToken?.let {
            val mapToBearer = mapAuthToDomain(it)
            localPreferences.saveAccessToken(mapToBearer)
            request.signWithToken(mapToBearer)
        }*/

        request.signWithToken(localKeyStore.loadBearerToken())
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

fun Request.signWithToken(token: Bearer) =
    newBuilder()
        .header("Authorization", "Bearer ${token.accessToken}")
        .build()