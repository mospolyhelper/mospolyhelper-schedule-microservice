package com.mospolytech.features.base.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureDi(modules: List<org.koin.core.module.Module>) {
    install(Koin) {
        modules(modules.plus(module { single<ApplicationConfig> { environment.config } }))
    }
}
