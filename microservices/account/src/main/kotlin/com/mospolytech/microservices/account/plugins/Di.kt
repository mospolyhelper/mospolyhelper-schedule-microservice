package com.mospolytech.microservices.account.plugins

import com.mospolytech.features.base.koin.Koin
import com.mospolytech.microservices.account.appModules
import io.ktor.server.application.*

fun Application.configureDi() {
    install(Koin) {
        modules(appModules)
    }
}