package com.mospolytech.microservices.schedule.plugins

import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.MpuPrincipal
import com.mospolytech.features.base.mpuAuth
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.server.locations.*
import io.ktor.http.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureSecurity() {
    install(Authentication) {
        mpuAuth(AuthConfigs.Mpu) {
            validate {
                MpuPrincipal(it.token)
            }
        }
    }
}
