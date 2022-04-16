package com.mospolytech.features.schedule.routes

import com.mospolytech.domain.schedule.model.schedule_info.ScheduleInfoObject
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.features.base.respondResult
import com.mospolytech.features.schedule.routes.model.ScheduleInfoRequest
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.scheduleInfoRoutesV1(
    repository: ScheduleRepository
) {
    route("/schedule") {
        route("/info") {
            get<ScheduleInfoRequest> {
                when (it.type) {
                    ScheduleInfoObject.Group -> call.respondResult(repository.getGroupInfo(it.key))
                    ScheduleInfoObject.Teacher -> call.respondResult(repository.getTeacherInfo(it.key))
                    ScheduleInfoObject.Student -> call.respondResult(repository.getStudentInfo(it.key))
                    ScheduleInfoObject.Place -> call.respondResult(repository.getPlaceInfo(it.key))
                    ScheduleInfoObject.Subject -> call.respondResult(repository.getSubjectInfo(it.key))
                    ScheduleInfoObject.LessonType -> call.respondResult(repository.getLessonTypeInfo(it.key))
                }
            }
        }

    }
}