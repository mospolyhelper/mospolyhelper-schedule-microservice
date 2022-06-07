package com.mospolytech.microservices.schedule

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.features.auth.authRoutesV1
import org.koin.ktor.ext.get
import com.mospolytech.features.base.plugins.*
import com.mospolytech.features.schedule.scheduleDataConversion
import com.mospolytech.features.schedule.scheduleRoutes
import io.ktor.server.application.*
import io.ktor.server.plugins.dataconversion.*

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
    setDataConversions()
    setRoutes()
    initDb()
}

fun Application.setRoutes() {
    scheduleRoutes(get(), get(), get(), get(), get())
    authRoutesV1(get(), get())
}

fun Application.setDataConversions() {
    install(DataConversion) {
        scheduleDataConversion()
    }
}

fun Application.initDb() {
    MosPolyDb.connectAndMigrate(environment.config)
}
