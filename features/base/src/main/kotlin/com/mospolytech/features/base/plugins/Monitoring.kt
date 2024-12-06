package com.mospolytech.features.base.plugins

import ch.qos.logback.classic.Logger
import com.mospolytech.domain.base.AppConfig
import com.mospolytech.domain.base.model.AdminLogLevel
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

private fun setupLog(adminLogLevel: AdminLogLevel) {
    val logLevel =
        when (adminLogLevel) {
            AdminLogLevel.FULL -> ch.qos.logback.classic.Level.TRACE
            AdminLogLevel.MIN -> ch.qos.logback.classic.Level.INFO
        }
    loggers.forEach { loggerName ->
        val logger = LoggerFactory.getLogger(loggerName) as? Logger ?: return@forEach
        logger.level = logLevel
    }
}

private val loggers =
    listOf(
        "org.eclipse.jetty",
        "io.netty",
        "org.quartz",
        "Exposed",
    )

fun Application.configureMonitoring(appConfig: AppConfig) {
    val logLevel =
        when (appConfig.adminLogLevel) {
            AdminLogLevel.FULL -> Level.TRACE
            AdminLogLevel.MIN -> Level.INFO
        }
    install(CallLogging) {
        level = logLevel
        filter { call -> call.request.path().startsWith("/") }
    }
    setupLog(appConfig.adminLogLevel)

//
//    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
//
//    install(MicrometerMetrics) {
//        registry = appMicrometerRegistry
//        // ...
//    }

//    routing {
//        get("/logs") {
//            if (call.request.queryParameters["key"] != appConfig.adminKey) {
//                call.respond(HttpStatusCode.Forbidden, "")
//                return@get
//            }
//
//            val logs = File("testFile.log").readText()
//
//            (LoggerFactory.getILoggerFactory() as LoggerContext).loggerList
//                .map { it.iteratorForAppenders() }
//                .forEach {
//                    while (it.hasNext()) {
//                        val appender = it.next()
//                        when (appender.name) {
//                            "FILE" -> (appender as FileAppender<*>).outputStream.flush()
//                        }
//                    }
//                }
//
//            call.respond(logs)
//        }
//    }
}
