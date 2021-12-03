package com.mospolytech.mph.auth

import com.mospolytech.mph.auth.plugins.*
import com.mospolytech.mph.features.schedule.scheduleRoutes
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureSecurity()
    configureRouting()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    setRoutes()
}

fun Application.setRoutes() {
    scheduleRoutes()
}
