package com.mospolytech.microservices.account.plugins

import com.mospolytech.features.base.OutputJsonConfig
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(OutputJsonConfig)
    }
}
