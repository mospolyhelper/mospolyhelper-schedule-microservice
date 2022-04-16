package com.mospolytech.features.schedule

import com.mospolytech.domain.personal.repository.PersonalRepository
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.features.schedule.routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.scheduleRoutes(
    scheduleRepository: ScheduleRepository,
    personalRepository: PersonalRepository
) {
    routing {
        sourcesRoutesV1(scheduleRepository)
        scheduleRoutesV1(scheduleRepository, personalRepository)
        scheduleInfoRoutesV1(scheduleRepository)
        lessonsRoutesV1(scheduleRepository)
        freePlaceRoutesV1(scheduleRepository)
    }
}