package com.mospolytech.features.schedule

import com.mospolytech.domain.personal.repository.PersonalRepository
import com.mospolytech.domain.schedule.repository.FreePlacesRepository
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.repository.ScheduleInfoRepository
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.scheduleRoutes(
    lessonsRepository: LessonsRepository,
    scheduleRepository: ScheduleRepository,
    scheduleInfoRepository: ScheduleInfoRepository,
    freePlacesRepository: FreePlacesRepository,
    personalRepository: PersonalRepository
) {
    routing {
        sourcesRoutesV1(scheduleRepository)
        scheduleRoutesV1(scheduleRepository, personalRepository)
        scheduleInfoRoutesV1(scheduleInfoRepository)
        lessonsRoutesV1(lessonsRepository, scheduleRepository)
        freePlaceRoutesV1(freePlacesRepository)
    }
}
