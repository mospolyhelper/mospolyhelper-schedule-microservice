package com.mospolytech.microservices.auth

import com.mospolytech.features.auth.authRoutesV1
import com.mospolytech.features.base.koin.get
import com.mospolytech.microservices.auth.plugins.*
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
    authRoutesV1(get(), get())
}