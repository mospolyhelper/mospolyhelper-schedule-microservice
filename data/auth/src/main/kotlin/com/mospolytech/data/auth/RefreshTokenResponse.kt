package com.mospolytech.data.auth

import com.mospolytech.domain.auth.TokenModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    @SerialName("accessToken")
    val accessToken: String? = null,
    @SerialName("refreshToken")
    val refreshToken: String? = null,
)

internal fun RefreshTokenResponse.toDomainModel(token: String): TokenModel {
    var guid: String? = null

    try {
        val jwt = accessToken ?: refreshToken
        guid = jwt?.parseJwtGuid()
    } catch (_: Exception) {
    }

    return TokenModel(
        token = token,
        guid = guid,
        jwt = accessToken,
        jwtRefresh = refreshToken,
    )
}
