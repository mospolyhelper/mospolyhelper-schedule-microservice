package com.mospolytech.features.base

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

suspend fun ApplicationCall.getTokenOrRespondError(): String? {
    val principal: MpuPrincipal? = authentication.principal()
    return if (principal == null) {
        respond(HttpStatusCode.Unauthorized)
        null
    } else {
        principal.token
    }
}