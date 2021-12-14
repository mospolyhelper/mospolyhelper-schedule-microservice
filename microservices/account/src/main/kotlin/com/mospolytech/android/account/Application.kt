package com.mospolytech.android.account

import com.mospolytech.android.account.plugins.*
import com.mospolytech.features.applications.applicationsRoutesV1
import com.mospolytech.features.base.koin.get
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureSecurity()
    configureRouting()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureDi()
    setRoutes()
}

fun Application.setRoutes() {
    applicationsRoutesV1(get())
}