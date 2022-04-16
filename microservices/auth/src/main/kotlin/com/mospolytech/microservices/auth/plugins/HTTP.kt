package com.mospolytech.microservices.auth.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*

fun Application.configureHTTP() {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        //anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

}
