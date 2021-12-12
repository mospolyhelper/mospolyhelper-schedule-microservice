package com.mospolytech.microservices.schedule.plugins

import com.mospolytech.features.base.koin.Koin
import com.mospolytech.microservices.schedule.appModules
import io.ktor.server.application.*

fun Application.configureDi() {
    install(Koin) {
        modules(appModules)
    }
}