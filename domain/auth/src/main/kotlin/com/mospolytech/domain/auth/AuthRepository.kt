package com.mospolytech.domain.auth

interface AuthRepository {
    suspend fun getToken(
        login: String,
        password: String,
    ): Result<String>

    suspend fun getTokenForMainUser(): String
}
