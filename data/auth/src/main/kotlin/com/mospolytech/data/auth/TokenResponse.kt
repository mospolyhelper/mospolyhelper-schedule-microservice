package com.mospolytech.data.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.impl.JWTParser
import com.auth0.jwt.interfaces.Payload
import com.mospolytech.domain.auth.TokenModel
import io.ktor.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("token")
    val token: String,
    @SerialName("guid")
    val guid: String? = null,
    @SerialName("jwt")
    val jwt: String? = null,
    @SerialName("jwt_refresh")
    val jwtRefresh: String? = null,
)

internal fun TokenResponse.toDomainModel(): TokenModel {
    var guid: String? = guid

    if (guid == null) {
        try {
            val jwt = jwt ?: jwtRefresh
            guid = jwt?.parseJwtGuid()
        } catch (_: Exception) {
        }
    }

    return TokenModel(
        token = token,
        guid = guid,
        jwt = jwt,
        jwtRefresh = jwtRefresh,
    )
}

private val jwtParser = JWTParser()

internal fun String.parseJwtGuid(): String? {
    return parseJwt().getClaim("IndividualGuid").asString()
}

private fun String.parseJwt(): Payload =
    JWT.decode(this)
        .payload
        .decodeBase64String()
        .let { jwtParser.parsePayload(it) }
