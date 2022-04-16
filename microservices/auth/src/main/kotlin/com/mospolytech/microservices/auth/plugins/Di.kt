package com.mospolytech.microservices.auth.plugins

import com.mospolytech.features.base.koin.Koin
import com.mospolytech.microservices.auth.appModules
import io.ktor.server.application.*

fun Application.configureDi() {
    install(Koin) {
        modules(appModules)
    }
}