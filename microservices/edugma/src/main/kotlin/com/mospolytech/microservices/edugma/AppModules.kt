package com.mospolytech.microservices.edugma

import com.mospolytech.data.applications.applicationsDataModule
import com.mospolytech.data.auth.authDataModule
import com.mospolytech.data.base.baseDataModule
import com.mospolytech.data.payments.paymentsDataModule
import com.mospolytech.data.peoples.peoplesDataModule
import com.mospolytech.data.performance.performanceDataModule
import com.mospolytech.data.personal.personalDataModule
import com.mospolytech.data.schedule.scheduleDataModule
import com.mospolytech.domain.applications.applicationsDomainModule
import com.mospolytech.domain.payments.paymentsDomainModule
import com.mospolytech.domain.peoples.peoplesDomainModule
import com.mospolytech.domain.perfomance.performanceDomainModule
import com.mospolytech.domain.personal.personalDomainModule
import com.mospolytech.domain.schedule.scheduleDomainModule
import com.mospolytech.features.applications.applicationsFeatureModule
import com.mospolytech.features.base.BaseFeatureModule
import com.mospolytech.features.payments.paymentsFeatureModule
import com.mospolytech.features.peoples.peoplesFeaturesModule
import com.mospolytech.features.performance.performanceFeaturesModule
import com.mospolytech.features.personal.personalFeaturesModule
import com.mospolytech.features.schedule.scheduleFeatureModule

val appModules = listOf(
    baseDataModule,
    BaseFeatureModule.di,

    authDataModule,

    applicationsDataModule,
    applicationsDomainModule,
    applicationsFeatureModule,

    paymentsDataModule,
    paymentsDomainModule,
    paymentsFeatureModule,

    personalDataModule,
    personalDomainModule,
    personalFeaturesModule,

    performanceDataModule,
    performanceDomainModule,
    performanceFeaturesModule,

    peoplesDataModule,
    peoplesDomainModule,
    peoplesFeaturesModule,

    scheduleDataModule,
    scheduleDomainModule,
    scheduleFeatureModule,
)
