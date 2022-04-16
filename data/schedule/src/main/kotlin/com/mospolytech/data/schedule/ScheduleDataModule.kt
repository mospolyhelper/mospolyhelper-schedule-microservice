package com.mospolytech.data.schedule

import com.mospolytech.data.schedule.converters.ApiScheduleConverter
import com.mospolytech.data.schedule.converters.LessonTeachersConverter
import com.mospolytech.data.schedule.repository.*
import com.mospolytech.data.schedule.service.ScheduleService
import com.mospolytech.domain.schedule.repository.*
import org.koin.dsl.module

val scheduleDataModule = module {
    single { ScheduleService(get()) }

    single { ApiScheduleConverter(get()) }
    single { LessonTeachersConverter(get()) }



    single<LessonsRepository> { LessonsRepositoryImpl(get(), get(), get()) }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get(), get()) }
    single<ScheduleInfoRepository> { ScheduleInfoRepositoryImpl(get()) }
    single<FreePlacesRepository> { FreePlacesRepositoryImpl(get()) }

    single<TeachersRepository> { TeachersRepositoryImpl() }
}