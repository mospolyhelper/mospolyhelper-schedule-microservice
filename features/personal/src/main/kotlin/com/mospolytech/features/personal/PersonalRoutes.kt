package com.mospolytech.features.personal

import com.mospolytech.domain.personal.repository.PersonalRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.utils.getTokenOrRespondError
import com.mospolytech.features.base.utils.respondResult
import com.mospolytech.features.base.toResponse
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.personalRoutesV1(repository: PersonalRepository) {
    routing {
        route("/personal") {
            get {
                call.respond(repository.getPersonalInfo("").toResponse())
            }
            route("/orders") {
                get {
                    call.respond(repository.getOrders().toResponse())
                }
            }
        }

        authenticate(AuthConfigs.Mpu, optional = true) {
            get("/personal2") {
                val token = call.getTokenOrRespondError() ?: return@get

                call.respondResult(repository.getPersonalInfo(token))
            }
        }
    }
}