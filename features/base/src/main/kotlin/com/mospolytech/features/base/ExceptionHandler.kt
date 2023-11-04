package com.mospolytech.features.base

import com.mospolytech.domain.base.exception.AuthenticationException
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.respondError(it: Throwable) {
    when (it) {
        is ClientRequestException ->
            when (it.response.status) {
                HttpStatusCode.BadRequest -> respondText("", status = HttpStatusCode.Forbidden)
                else -> respondText("", status = HttpStatusCode.BadGateway)
            }
        is AuthenticationException -> respondText("", status = HttpStatusCode.Unauthorized)
        else -> respondText("", status = HttpStatusCode.BadGateway)
    }
}
