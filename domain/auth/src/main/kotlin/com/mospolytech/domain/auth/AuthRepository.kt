package com.mospolytech.domain.auth

interface AuthRepository {
    suspend fun getToken(
        login: String,
        password: String,
    ): Result<TokenModel>

    suspend fun refreshToken(refreshTokenModel: RefreshTokenModel): Result<TokenModel>

    suspend fun getTokenForMainUser(): String

    suspend fun getAccount(token: String, guid: String?): Result<AccountsModel>
}
