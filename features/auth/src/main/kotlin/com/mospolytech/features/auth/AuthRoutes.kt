package com.mospolytech.features.auth

import com.mospolytech.domain.auth.AuthRepository
import com.mospolytech.domain.personal.repository.PersonalRepository
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

fun Application.authRoutesV1(
    repository: AuthRepository,
    personalRepository: PersonalRepository
) {
    routing {
        post("login") {
            val loginRequest = call.receive<LoginRequest>()
            val token = repository.getToken(loginRequest.login, loginRequest.password)
            token.onSuccess {
                call.respond(TokenResponse(it))
            }.onFailure {
                when (it) {
                    is ClientRequestException -> when (it.response.status) {
                        HttpStatusCode.BadRequest -> call.respondText("", status = HttpStatusCode.Forbidden)
                        else -> call.respondText("", status = HttpStatusCode.BadGateway)
                    }
                    else -> call.respondText("", status = HttpStatusCode.BadGateway)
                }
            }
        }

        get("validate") {
            val userTypeField = call.parameters[ValidationFields.UserType.toString().lowercase()]
            val scheduleKeyField = call.parameters[ValidationFields.ScheduleKey.toString().lowercase()]

            personalRepository.getPersonalInfo()
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

data class TokenResponse(
    val token: String
)