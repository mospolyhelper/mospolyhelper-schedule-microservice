package com.mospolytech.microservices.account.plugins

import com.mospolytech.features.base.OutputJsonConfig
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.*
import io.ktor.server.application.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(OutputJsonConfig)
    }
}
