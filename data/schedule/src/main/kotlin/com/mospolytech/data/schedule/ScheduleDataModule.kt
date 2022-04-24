package com.mospolytech.data.schedule

import com.mospolytech.data.schedule.converters.*
import com.mospolytech.data.schedule.local.ScheduleCacheDS
import com.mospolytech.data.schedule.repository.*
import com.mospolytech.data.schedule.service.ScheduleService
import com.mospolytech.domain.schedule.repository.*
import org.koin.dsl.module

val scheduleDataModule = module {
    single { ScheduleService(get()) }
    single { ScheduleCacheDS() }

    single { ApiScheduleConverter(get(), get(), get(), get(), get()) }
    single { LessonSubjectConverter(get()) }
    single { LessonTypeConverter(get()) }
    single { LessonTeachersConverter(get()) }
    single { LessonGroupsConverter(get()) }
    single { LessonPlacesConverter(get()) }



    single<LessonsRepository> { LessonsRepositoryImpl(get(), get(), get()) }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get(), get(), get(), get(), get(), get()) }
    single<ScheduleInfoRepository> { ScheduleInfoRepositoryImpl(get(), get(), get(), get(), get()) }
    single<FreePlacesRepository> { FreePlacesRepositoryImpl(get(), get()) }

    single<LessonSubjectsRepository> { LessonSubjectsRepositoryImpl() }
    single<LessonTypesRepository> { LessonTypesRepositoryImpl() }
    single<TeachersRepository> { TeachersRepositoryImpl() }
    single<GroupsRepository> { GroupsRepositoryImpl() }
    single<PlacesRepository> { PlacesRepositoryImpl() }
}