package com.mospolytech.microservices.schedule.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

//    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
//
//    install(MicrometerMetrics) {
//        registry = appMicrometerRegistry
//        // ...
//    }
//
//
//    routing {
//        get("/metrics-micrometer") {
//            call.respond(appMicrometerRegistry.scrape())
//        }
//    }
}
