package com.mospolytech.features.services.performance

import com.mospolytech.domain.services.performance.PerformanceRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.utils.getTokenOrRespondError
import com.mospolytech.features.base.utils.respondResult
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.routing.*

fun Application.performanceRoutesV1(repository: PerformanceRepository) {
    routing {
        authenticate(AuthConfigs.MPU, optional = true) {
            route("/performance") {
                get("/periods") {
                    val token = call.getTokenOrRespondError() ?: return@get
                    call.respondResult(repository.getPeriods(token))
                }
                get<PeriodRequest> {
                    val token = call.getTokenOrRespondError() ?: return@get
                    call.respondResult(repository.getPerformance(it.periodId, token))
                }
            }
        }
    }
}

@Location("/{periodId}")
data class PeriodRequest(
    val periodId: String,
)
