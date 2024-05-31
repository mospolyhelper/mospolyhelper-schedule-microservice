package com.mospolytech.features.base.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    install(CORS) {
        for (httpMethod in HttpMethod.DefaultMethods) {
            allowMethod(httpMethod)
        }
        allowHeaders { true }
        allowCredentials = true
        allowNonSimpleContentTypes = true
        // TODO remove
        allowHost("localhost:8080")
        allowHost("edugma.github.io", schemes = listOf("http", "https"))
        allowHost("*.edugma.com", schemes = listOf("http", "https"))
    }
}
