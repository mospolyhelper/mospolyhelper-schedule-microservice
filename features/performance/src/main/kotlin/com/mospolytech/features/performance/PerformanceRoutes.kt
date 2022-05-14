package com.mospolytech.features.performance

import com.mospolytech.domain.perfomance.repository.PerformanceRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.utils.getTokenOrRespondError
import com.mospolytech.features.base.utils.respondResult
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.routing.*

fun Application.performanceRoutesV1(repository: PerformanceRepository) {
    routing {
        authenticate(AuthConfigs.Mpu, optional = true) {
            route("/performance") {
                route("/semesters") {
                    get<SemesterRequest> {
                        val token = call.getTokenOrRespondError() ?: return@get
                        call.respondResult(repository.getPerformance(it.semester, token))
                    }
                    get<AllSemesters> {
                        val token = call.getTokenOrRespondError() ?: return@get
                        call.respondResult(repository.getPerformance(null, token))
                    }
                    get {
                        val token = call.getTokenOrRespondError() ?: return@get
                        call.respondResult(repository.getSemesters(token))
                    }
                }
                route("/courses") {
                    get {
                        val token = call.getTokenOrRespondError() ?: return@get
                        call.respondResult(repository.getCourses(token))
                    }
                }
            }
        }
    }
}

@Location("/{semester}")
data class SemesterRequest(
    val semester: String? = null
)

@Location("/")
object AllSemesters