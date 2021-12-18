package com.mospolytech.microservices.account


import com.mospolytech.features.applications.applicationsRoutesV1
import com.mospolytech.features.base.koin.get
import com.mospolytech.features.payments.paymentsDataConversion
import com.mospolytech.features.payments.paymentsRoutesV1
import com.mospolytech.features.performance.performanceRoutesV1
import com.mospolytech.features.personal.personalRoutesV1
import com.mospolytech.microservices.account.plugins.*
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
    setRoutes()
    setDataConversions()
}

fun Application.setRoutes() {
    applicationsRoutesV1(get())
    paymentsRoutesV1(get())
    personalRoutesV1(get())
    performanceRoutesV1(get())
}

fun Application.setDataConversions() {
    install(DataConversion) {
        paymentsDataConversion()
    }
}