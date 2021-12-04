package com.mospolytech.mph.auth

import com.mospolytech.mph.auth.plugins.*
import com.mospolytech.mph.features.base.koin.get
import com.mospolytech.mph.features.schedule.scheduleDataConversion
import com.mospolytech.mph.features.schedule.scheduleRoutes
import io.ktor.server.application.*
import io.ktor.server.plugins.*

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
    setDataConversions()
    setRoutes()
}

fun Application.setRoutes() {
    scheduleRoutes(get())
}

fun Application.setDataConversions() {
    install(DataConversion) {
        scheduleDataConversion()
    }
}