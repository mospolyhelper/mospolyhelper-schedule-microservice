package com.mospolytech.data.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mospolytech.domain.auth.AuthRepository
import java.util.*

class AuthRepositoryImpl(
    private val service: AuthService
) : AuthRepository {

    companion object {
        const val SECRET = "secret_key" //todo
        const val LK_TOKEN = "mospolytechLkToken"
    }

    override suspend fun getToken(login: String, password: String): Result<String> {
        return runCatching {
            service.getToken(login, password).token.createJwt()
        }
    }

    private fun String.createJwt() = JWT
        .create()
        .withClaim(LK_TOKEN, this)
        .withExpiresAt(Date(Long.MAX_VALUE))
        .sign(Algorithm.HMAC256(SECRET))
}