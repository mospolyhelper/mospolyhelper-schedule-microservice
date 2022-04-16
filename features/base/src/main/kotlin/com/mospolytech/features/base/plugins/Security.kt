package com.mospolytech.features.base.plugins

import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.MpuPrincipal
import com.mospolytech.features.base.mpuAuth
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureSecurity() {
    install(Authentication) {
        mpuAuth(AuthConfigs.Mpu) {
            validate {
                MpuPrincipal(it.token)
            }
        }
    }
}
