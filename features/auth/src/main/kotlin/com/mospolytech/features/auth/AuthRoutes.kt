package com.mospolytech.features.auth

import com.mospolytech.domain.auth.AuthRepository
import com.mospolytech.domain.auth.GetJwtRefreshTokenUseCase
import com.mospolytech.domain.auth.GetJwtTokenUseCase
import com.mospolytech.domain.auth.ParseRefreshTokenUseCase
import com.mospolytech.domain.services.personal.PersonalRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.MpuPrincipal
import com.mospolytech.features.base.utils.getPrincipalOrRespondError
import com.mospolytech.features.base.utils.respondResult
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Application.authRoutesV1(
    repository: AuthRepository,
    personalRepository: PersonalRepository,
    getJwtTokenUseCase: GetJwtTokenUseCase,
    getJwtRefreshTokenUseCase: GetJwtRefreshTokenUseCase,
    parseRefreshTokenUseCase: ParseRefreshTokenUseCase,
) {
    routing {
        // Deprecated
        post("login") {
            loginPost(repository, getJwtTokenUseCase, getJwtRefreshTokenUseCase)
        }

        route("/auth") {
            post("/login") {
                loginPost(repository, getJwtTokenUseCase, getJwtRefreshTokenUseCase)
            }

            post("/refresh") {
                val refreshRequest = call.receive<RefreshRequest>()
                val refreshTokenModel = parseRefreshTokenUseCase(refreshRequest.refreshToken)
                val newToken = repository.refreshToken(refreshTokenModel).mapCatching {
                    val tokenJwt = getJwtTokenUseCase(it)
                    val refreshTokenJwt = getJwtRefreshTokenUseCase(it)

                    TokenResponse(
                        token = tokenJwt,
                        accessToken = tokenJwt,
                        refreshToken = refreshTokenJwt,
                    )
                }

                call.respondResult(newToken)
            }

            authenticate(AuthConfigs.MPU, optional = true) {
                get("/accounts") {
                    val principal = call.getPrincipalOrRespondError() ?: return@get
                    val accounts = repository.getAccount(
                        token = principal.token,
                        guid = principal.guid,
                    )

                    call.respondResult(accounts)
                }
            }
        }

        authenticate(AuthConfigs.MPU, optional = true) {
            get("validate") {
                val principal: MpuPrincipal? = call.authentication.principal()

                call.respondResult(personalRepository.getPersonalInfo(principal!!.token))
            }
        }
    }
}

private suspend fun RoutingContext.loginPost(
    repository: AuthRepository,
    getJwtTokenUseCase: GetJwtTokenUseCase,
    getJwtRefreshTokenUseCase: GetJwtRefreshTokenUseCase,
) {
    val loginRequest = call.receive<LoginRequest>()
    val token = repository.getToken(
        loginRequest.login,
        loginRequest.password,
    ).mapCatching {
        val tokenJwt = getJwtTokenUseCase(it)
        val refreshTokenJwt = getJwtRefreshTokenUseCase(it)

        TokenResponse(
            token = tokenJwt,
            accessToken = tokenJwt,
            refreshToken = refreshTokenJwt,
        )
    }

    call.respondResult(token)
}

@Serializable
data class LoginRequest(
    val login: String,
    val password: String,
)

@Serializable
data class RefreshRequest(
    @SerialName("refreshToken")
    val refreshToken: String,
)

@Serializable
data class TokenResponse(
    @Deprecated("Use field accessToken")
    @SerialName("token")
    val token: String,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String? = null,
)
