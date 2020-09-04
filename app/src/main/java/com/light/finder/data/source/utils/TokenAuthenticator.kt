package com.light.finder.data.source.utils

import com.light.domain.AuthRepository
import com.light.usecases.GetBearerTokenUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // This is a synchronous call
        // This runs when 401 error happens
        val updatedToken = getNewToken()
        return response.request.newBuilder()
            .header("Authorization", updatedToken)
            .build()
    }

    private fun getNewToken(): String {

        //TODO get new token and return it

//        val newToken = "${authTokenResponse.tokenType} ${authTokenResponse.accessToken}"
//        SharedPreferenceUtils.saveString(Constants.PreferenceKeys.USER_ACCESS_TOKEN, newToken)
        return "newToken"
    }
}