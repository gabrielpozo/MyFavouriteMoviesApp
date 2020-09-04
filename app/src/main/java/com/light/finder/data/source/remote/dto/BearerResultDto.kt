package com.light.finder.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class BearerResultDto(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Long,
    @SerializedName("token_type") val tokenType: String
)