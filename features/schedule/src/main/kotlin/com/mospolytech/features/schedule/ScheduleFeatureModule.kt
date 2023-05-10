package com.mospolytech.features.schedule

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val scheduleFeatureModule = module {
    singleOf(::ScheduleJobLauncher)
    singleOf(::ScheduleUpdateJob)
}
