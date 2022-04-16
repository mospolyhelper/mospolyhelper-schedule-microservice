package com.mospolytech.features.base.utils

import com.mospolytech.features.base.respondError
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.logging.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val ErrorLogger: Logger = LoggerFactory.getLogger("Application")

suspend inline fun <reified T : Any> ApplicationCall.respondResult(result: Result<T>) {
    result.onSuccess {
        respond(it)
    }.onFailure {
        ErrorLogger.error(it)
        respondError(it)
    }
}