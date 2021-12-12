package com.mospolytech.microservices.schedule

import com.mospolytech.data.base.baseDataModule
import com.mospolytech.data.schedule.scheduleDataModule
import com.mospolytech.domain.schedule.scheduleDomainModule
import com.mospolytech.features.schedule.scheduleFeatureModule

val appModules = listOf(
    baseDataModule,
    scheduleDataModule,


    scheduleDomainModule,


    scheduleFeatureModule,
)