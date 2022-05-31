package com.mospolytech.microservices.auth

import com.mospolytech.features.auth.authRoutesV1
import org.koin.ktor.ext.get
import com.mospolytech.features.base.plugins.*
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
    configureDi(appModules)
    setRoutes()
}

fun Application.setRoutes() {
    authRoutesV1(get(), get())
}