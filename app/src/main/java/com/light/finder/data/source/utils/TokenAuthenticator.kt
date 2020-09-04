package com.light.finder.data.source.utils

import com.light.domain.AuthRepository
import com.light.domain.model.Bearer
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator(private val authRepository: AuthRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = when {
        response.retryCount > 2 -> null
        else -> runBlocking {
            response.createSignedRequest()
        }
    }

    private suspend fun Response.createSignedRequest(): Request? = try {
        val accessToken = authRepository.getBearerToken()
        request.signWithToken(accessToken as Bearer) //TODO find a way to get Bearer from repo
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

fun Request.signWithToken(accessToken: Bearer) =
    newBuilder()
        .header("Authorization", "Bearer ${accessToken.accessToken}")
        .build()