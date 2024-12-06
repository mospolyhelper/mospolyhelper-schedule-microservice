package com.mospolytech.features.services.performance

import com.mospolytech.domain.services.performance.PerformanceRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.utils.getTokenOrRespondError
import com.mospolytech.features.base.utils.respondResult
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.performanceRoutesV1(repository: PerformanceRepository) {
    routing {
        authenticate(AuthConfigs.MPU, optional = true) {
            route("/performance") {
                get("/{periodId?}") {
                    val token = call.getTokenOrRespondError() ?: return@get

                    val periodId = call.parameters["periodId"]
                    call.respondResult(repository.getPerformance(periodId, token))
                }
            }
        }
    }
}
