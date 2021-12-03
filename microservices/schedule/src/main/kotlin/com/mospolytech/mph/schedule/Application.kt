package com.mospolytech.mph.schedule

import com.mospolytech.mph.features.schedule.scheduleRoutes
import com.mospolytech.mph.schedule.plugins.*
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
    setRoutes()
}

fun Application.setRoutes() {
    scheduleRoutes()
}
