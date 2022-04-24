package com.mospolytech.features.base.plugins

import com.mospolytech.features.base.koin.Koin
import io.ktor.server.application.*

fun Application.configureDi(modules: List<org.koin.core.module.Module>) {
    install(Koin) {
        modules(modules)
    }
}