package com.mospolytech.data.schedule

import com.mospolytech.data.schedule.converters.*
import com.mospolytech.data.schedule.local.ScheduleCacheDS
import com.mospolytech.data.schedule.remote.LessonDateTimesRemoteDS
import com.mospolytech.data.schedule.remote.LessonsRemoteDS
import com.mospolytech.data.schedule.repository.*
import com.mospolytech.data.schedule.service.ScheduleService
import com.mospolytech.domain.schedule.repository.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val scheduleDataModule = module {
    singleOf(::ScheduleService)
    singleOf(::ScheduleCacheDS)
    singleOf(::LessonsRemoteDS)
    singleOf(::LessonDateTimesRemoteDS)

    singleOf(::ApiScheduleConverter)
    singleOf(::LessonSubjectConverter)
    singleOf(::LessonTypeConverter)
    singleOf(::LessonTeachersConverter)
    singleOf(::LessonGroupsConverter)
    singleOf(::LessonPlacesConverter)
    singleOf(::LessonDateTimeConverter)
    singleOf(::LessonConverter)

    singleOf(::LessonsRepositoryImpl) { bind<LessonsRepository>() }
    singleOf(::ScheduleRepositoryImpl) { bind<ScheduleRepository>() }
    singleOf(::ScheduleInfoRepositoryImpl) { bind<ScheduleInfoRepository>() }
    singleOf(::FreePlacesRepositoryImpl) { bind<FreePlacesRepository>() }

    singleOf(::LessonSubjectsRepositoryImpl) { bind<LessonSubjectsRepository>() }
    singleOf(::LessonTypesRepositoryImpl) { bind<LessonTypesRepository>() }
    singleOf(::TeachersRepositoryImpl) { bind<TeachersRepository>() }
    singleOf(::GroupsRepositoryImpl) { bind<GroupsRepository>() }
    singleOf(::PlacesRepositoryImpl) { bind<PlacesRepository>() }
}