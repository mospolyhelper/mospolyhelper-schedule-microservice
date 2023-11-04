package com.mospolytech.features.base.plugins

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.FileAppender
import com.mospolytech.domain.base.AppConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.io.File

fun Application.configureMonitoring(appConfig: AppConfig) {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

//
//    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
//
//    install(MicrometerMetrics) {
//        registry = appMicrometerRegistry
//        // ...
//    }

    routing {
        get("/logs") {
            if (call.request.queryParameters["key"] != appConfig.adminKey) {
                call.respond(HttpStatusCode.Forbidden, "")
                return@get
            }

            val logs = File("testFile.log").readText()

            (LoggerFactory.getILoggerFactory() as LoggerContext).loggerList
                .map { it.iteratorForAppenders() }
                .forEach {
                    while (it.hasNext()) {
                        val appender = it.next()
                        when (appender.name) {
                            "FILE" -> (appender as FileAppender<*>).outputStream.flush()
                        }
                    }
                }

            call.respond(logs)
        }
    }
}
