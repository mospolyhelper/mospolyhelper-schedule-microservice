package com.mospolytech.features.auth

import com.mospolytech.domain.auth.AuthRepository
import com.mospolytech.domain.personal.repository.PersonalRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.MpuPrincipal
import com.mospolytech.features.base.utils.respondResult
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.Serializable

fun Application.authRoutesV1(
    repository: AuthRepository,
    personalRepository: PersonalRepository
) {
    routing {
        post("login") {
            val loginRequest = call.receive<LoginRequest>()
            val token = repository.getToken(loginRequest.login, loginRequest.password)
            call.respondResult(token)
        }

        authenticate(AuthConfigs.Mpu, optional = true) {
            get("validate") {
                val principal: MpuPrincipal? = call.authentication.principal()
                val userTypeField = call.parameters[ValidationFields.UserType.toString().lowercase()]
                val scheduleKeyField = call.parameters[ValidationFields.ScheduleKey.toString().lowercase()]

                call.respondResult(personalRepository.getPersonalInfo(principal!!.token))
            }
        }
    }
}

enum class ValidationFields {
    UserType,
    ScheduleKey
}

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class TokenResponse(
    val token: String
)