package com.mospolytech.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.impl.JWTParser
import com.auth0.jwt.interfaces.Payload
import com.mospolytech.domain.base.model.MpuPrincipalFields
import io.ktor.util.*
import java.util.*

class ParseRefreshTokenUseCase {
    operator fun invoke(jwt: String): RefreshTokenModel {
        val payload = jwt.parseJwt()
        return RefreshTokenModel(
            token = payload.getClaim(MpuPrincipalFields.MpuLkToken).asString(),
            guid = payload.getClaim(MpuPrincipalFields.MpuUserGuid).asString(),
            jwtRefresh = payload.getClaim(MpuPrincipalFields.MpuJwt).asString(),
        )
    }

    private val jwtParser = JWTParser()

    private fun String.parseJwt(): Payload =
        JWT.decode(this)
            .payload
            .decodeBase64String()
            .let { jwtParser.parsePayload(it) }
}
