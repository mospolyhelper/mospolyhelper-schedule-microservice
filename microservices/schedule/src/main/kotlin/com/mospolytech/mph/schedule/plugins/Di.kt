package com.mospolytech.mph.schedule.plugins

import com.mospolytech.mph.features.base.koin.Koin
import com.mospolytech.mph.schedule.appModules
import io.ktor.server.application.*

fun Application.configureDi() {
    install(Koin) {
        modules(appModules)
    }
}