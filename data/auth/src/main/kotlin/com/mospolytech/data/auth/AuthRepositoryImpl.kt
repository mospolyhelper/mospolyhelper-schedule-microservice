package com.mospolytech.data.auth

import com.mospolytech.domain.auth.AuthRepository
import com.mospolytech.domain.base.AppConfig
import java.util.*

class AuthRepositoryImpl(
    private val service: AuthService,
    private val appConfig: AppConfig,
) : AuthRepository {
    override suspend fun getToken(
        login: String,
        password: String,
    ): Result<String> {
        return runCatching {
            service.getToken(login, password).token
        }
    }

    override suspend fun getTokenForMainUser(): String {
        val login = appConfig.mainLkLogin
        val password = appConfig.mainLkPassword
        return service.getToken(login, password).token
    }
}
