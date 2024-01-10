package com.mospolytech.features.base.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    install(CORS) {
        // TODO remove
        allowHost("localhost:8080")
        allowHost("edugma.github.io")
        allowHeader(HttpHeaders.ContentType)
    }
}
