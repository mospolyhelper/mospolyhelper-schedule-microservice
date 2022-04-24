package com.mospolytech.features.base.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    install(Locations) {
    }

    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()