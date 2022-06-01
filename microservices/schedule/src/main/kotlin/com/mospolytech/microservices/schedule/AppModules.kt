package com.mospolytech.microservices.schedule

import com.mospolytech.data.auth.authDataModule
import com.mospolytech.data.base.baseDataModule
import com.mospolytech.data.peoples.peoplesDataModule
import com.mospolytech.data.personal.personalDataModule
import com.mospolytech.data.schedule.scheduleDataModule
import com.mospolytech.domain.peoples.peoplesDomainModule
import com.mospolytech.domain.personal.personalDomainModule
import com.mospolytech.domain.schedule.scheduleDomainModule
import com.mospolytech.features.schedule.scheduleFeatureModule

val appModules = listOf(
    baseDataModule,
    scheduleDataModule,


    scheduleDomainModule,


    scheduleFeatureModule,

    authDataModule,
    personalDomainModule,
    personalDataModule,
    peoplesDomainModule,
    peoplesDataModule
)