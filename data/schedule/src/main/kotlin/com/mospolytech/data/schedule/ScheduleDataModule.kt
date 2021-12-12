package com.mospolytech.data.schedule

import com.mospolytech.data.schedule.converters.ApiScheduleConverter
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import org.koin.dsl.module

val scheduleDataModule = module {
    single { ScheduleService(get()) }
    single { ApiScheduleConverter() }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get(), get()) }
}