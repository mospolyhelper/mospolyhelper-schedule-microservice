package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.base.AppConfig
import com.mospolytech.domain.personal.PersonalRepository
import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.features.schedule.ScheduleJobLauncher
import com.mospolytech.features.schedule.routes.model.ScheduleRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.scheduleRoutesV1(
    repository: ScheduleRepository,
    userRepository: PersonalRepository,
    scheduleJobLauncher: ScheduleJobLauncher,
    appConfig: AppConfig,
) {
    route("/schedules") {
        route("/compact") {
            get {
                call.respond(repository.getCompactSchedule())
            }
            get<ScheduleRequest> {
                call.respond(repository.getCompactSchedule(it.key, it.type))
            }
            post("/complex") {
                val filter = call.receive<ScheduleComplexFilter>()
                call.respond(repository.getCompactSchedule(filter))
            }
        }
//        authenticate(AuthConfigs.MPU, optional = true) {
//            get("my") {
//                val token = call.getTokenOrRespondError() ?: return@get
//
//                call.respondResult(
//                    userRepository.getPersonalInfo(token).map {
//                        val id = repository.findGroupByTitle(it.name)
//                        id?.let {
//                            repository.getCompactSchedule(id, ScheduleSourceTypes.Group)
//                        }
//                    },
//                )
//            }
//        }
        route("/update-schedules") {
            get {
                if (call.request.queryParameters["key"] != appConfig.adminKey) {
                    call.respond(HttpStatusCode.Forbidden, "")
                    return@get
                }
                val recreateDb = call.request.queryParameters["recreate"] == "1"
                if (recreateDb) {
                    repository.updateData(recreateDb = true)
                } else {
                    scheduleJobLauncher.launchNow()
                }
                call.respond("updated")
            }
        }
    }
}
