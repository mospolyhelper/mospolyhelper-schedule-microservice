package com.mospolytech.mph.auth.plugins

import com.mospolytech.mph.auth.appModules
import com.mospolytech.mph.features.base.koin.Koin
import io.ktor.server.application.*

fun Application.configureDi() {
    install(Koin) {
        modules(appModules)
    }
}