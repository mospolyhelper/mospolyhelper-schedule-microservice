package com.mospolytech.mph.data.schedule

import org.koin.dsl.module

val scheduleDataModule = module {
    single { ScheduleService(get()) }
    single { ScheduleRepositoryImpl() }
}