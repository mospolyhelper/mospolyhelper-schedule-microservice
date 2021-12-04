package com.mospolytech.mph.auth

import com.mospolytech.mph.data.base.baseDataModule
import com.mospolytech.mph.data.schedule.scheduleDataModule
import com.mospolytech.mph.domain.schedule.scheduleDomainModule
import com.mospolytech.mph.features.schedule.scheduleFeatureModule

val appModules = listOf(
    baseDataModule,
    scheduleDataModule,


    scheduleDomainModule,


    scheduleFeatureModule,
)