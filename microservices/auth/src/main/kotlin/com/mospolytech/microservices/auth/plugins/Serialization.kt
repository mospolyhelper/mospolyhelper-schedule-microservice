package com.mospolytech.microservices.auth.plugins

import com.mospolytech.features.base.OutputJsonConfig
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.*
import io.ktor.server.application.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(OutputJsonConfig)
    }
}
