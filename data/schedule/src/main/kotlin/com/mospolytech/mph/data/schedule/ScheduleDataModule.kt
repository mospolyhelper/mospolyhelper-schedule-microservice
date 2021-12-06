package com.mospolytech.mph.data.schedule

import com.mospolytech.mph.data.schedule.converters.ApiScheduleConverter
import com.mospolytech.mph.domain.schedule.repository.ScheduleRepository
import org.koin.dsl.module

val scheduleDataModule = module {
    single { ScheduleService(get()) }
    single { ApiScheduleConverter() }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get(), get()) }
}