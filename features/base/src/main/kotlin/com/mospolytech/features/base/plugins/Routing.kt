package com.mospolytech.features.base.plugins

import com.mospolytech.domain.base.exception.AuthorizationException
import com.mospolytech.domain.base.exception.InvalidCredentialsException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    install(Locations) {
    }

    install(StatusPages) {
        exception<InvalidCredentialsException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
    }
}
