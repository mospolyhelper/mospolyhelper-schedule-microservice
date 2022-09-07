package com.mospolytech.microservices.account

import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.features.applications.applicationsRoutesV1
import com.mospolytech.features.auth.authRoutesV1
import com.mospolytech.features.base.plugins.*
import com.mospolytech.features.payments.paymentsDataConversion
import com.mospolytech.features.payments.paymentsRoutesV1
import com.mospolytech.features.peoples.peoplesRoutesV1
import com.mospolytech.features.performance.performanceRoutesV1
import com.mospolytech.features.personal.personalRoutesV1
import io.ktor.server.application.*
import io.ktor.server.plugins.dataconversion.*
import org.koin.ktor.ext.get

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
    setDataConversions()
    initDb()
}

fun Application.setRoutes() {
    authRoutesV1(get(), get())
    applicationsRoutesV1(get())
    paymentsRoutesV1(get())
    personalRoutesV1(get())
    performanceRoutesV1(get())
    peoplesRoutesV1(get(), get())
}

fun Application.setDataConversions() {
    install(DataConversion) {
        paymentsDataConversion()
    }
}

fun Application.initDb() {
    MosPolyDb.connectAndMigrate(environment.config)
}
