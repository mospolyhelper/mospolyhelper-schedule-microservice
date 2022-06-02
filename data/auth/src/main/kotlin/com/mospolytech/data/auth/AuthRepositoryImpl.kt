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
            val lkToken = service.getToken(login, password).token
            JWT.create()
                .withClaim(LK_TOKEN, lkToken)
                .withExpiresAt(Date(Long.MAX_VALUE))
                .sign(Algorithm.HMAC256(SECRET))
        }
    }
}