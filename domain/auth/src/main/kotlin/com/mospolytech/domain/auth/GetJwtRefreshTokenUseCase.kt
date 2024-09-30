package com.mospolytech.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.mospolytech.domain.base.AppConfig
import com.mospolytech.domain.base.model.MpuPrincipalFields
import java.util.*

class GetJwtRefreshTokenUseCase(
    private val appConfig: AppConfig,
) {
    operator fun invoke(model: TokenModel): String {
        return model.createRefreshJwt(appConfig.jwtSecret)
    }

    private fun TokenModel.createRefreshJwt(secretKey: String): String {
        return JWT
            .create()
            // MpuLkToken не устаревает и мы будем возвращать его
            .withClaim(MpuPrincipalFields.MpuLkToken, this.token)
            .withNullableClaim(MpuPrincipalFields.MpuUserGuid, this.guid)
            .withNullableClaim(MpuPrincipalFields.MpuJwt, this.jwtRefresh)
            .withExpiresAt(Date(Long.MAX_VALUE))
            .sign(Algorithm.HMAC256(secretKey))
    }

    private fun JWTCreator.Builder.withNullableClaim(name: String, value: String?): JWTCreator.Builder {
        return if (value == null) {
            this
        } else {
            this.withClaim(name, value)
        }
    }
}
