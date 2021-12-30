package com.mospolytech.data.auth

import com.mospolytech.domain.auth.AuthRepository

class AuthRepositoryImpl(
    private val service: AuthService
) : AuthRepository {
    override suspend fun getToken(login: String, password: String): Result<String> {
        return runCatching {
            service.getToken(login, password).token
        }
    }
}