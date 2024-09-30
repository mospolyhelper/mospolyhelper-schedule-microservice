package com.mospolytech.data.auth

import com.mospolytech.domain.auth.AccountsModel
import com.mospolytech.domain.auth.AuthRepository
import com.mospolytech.domain.auth.GetJwtRefreshTokenUseCase
import com.mospolytech.domain.auth.GetJwtTokenUseCase
import com.mospolytech.domain.auth.RefreshTokenModel
import com.mospolytech.domain.auth.TokenModel
import com.mospolytech.domain.base.AppConfig

class AuthRepositoryImpl(
    private val service: AuthService,
    private val appConfig: AppConfig,
    private val getJwtTokenUseCase: GetJwtTokenUseCase,
    private val getJwtRefreshTokenUseCase: GetJwtRefreshTokenUseCase,
) : AuthRepository {
    override suspend fun getToken(
        login: String,
        password: String,
    ): Result<TokenModel> {
        return runCatching {
            val response = service.getToken(login, password)

            response.toDomainModel()
        }
    }

    override suspend fun refreshToken(refreshTokenModel: RefreshTokenModel): Result<TokenModel> {
        return if (refreshTokenModel.jwtRefresh == null) {
            Result.success(
                TokenModel(
                    token = refreshTokenModel.token,
                    guid = refreshTokenModel.guid,
                    jwt = null,
                    jwtRefresh = null,
                )
            )
        } else {
            runCatching {
                val response = service.refreshToken(refreshTokenModel.jwtRefresh!!)

                response.toDomainModel(refreshTokenModel.token)
            }
        }
    }

    override suspend fun getTokenForMainUser(): String {
        val login = appConfig.mainLkLogin
        val password = appConfig.mainLkPassword
        return service.getToken(login, password).token
    }

    override suspend fun getAccount(token: String, guid: String?): Result<AccountsModel> {
        return runCatching {
            val response = service.getAccounts(token)

            val accessToken = getJwtTokenUseCase()

            response.toModel(guid)
        }
    }
}
