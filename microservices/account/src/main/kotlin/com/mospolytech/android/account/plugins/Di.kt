package com.mospolytech.android.account.plugins

import com.mospolytech.android.account.appModules
import com.mospolytech.features.base.koin.Koin
import io.ktor.server.application.*

fun Application.configureDi() {
    install(Koin) {
        modules(appModules)
    }
}