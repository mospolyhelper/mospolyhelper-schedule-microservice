package com.mospolytech.features.services.personal

import com.mospolytech.domain.services.personal.PersonalRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.utils.getTokenOrRespondError
import com.mospolytech.features.base.utils.respondResult
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.personalRoutesV1(repository: PersonalRepository) {
    routing {
        authenticate(AuthConfigs.MPU, optional = true) {
            get("/personal") {
                val token = call.getTokenOrRespondError() ?: return@get
                call.respondResult(repository.getPersonalInfo(token))
            }
        }
    }
}
