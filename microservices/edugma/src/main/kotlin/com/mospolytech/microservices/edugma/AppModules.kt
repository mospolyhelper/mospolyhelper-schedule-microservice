package com.mospolytech.microservices.edugma

import com.mospolytech.data.auth.authDataModule
import com.mospolytech.data.base.baseDataModule
import com.mospolytech.data.peoples.peoplesDataModule
import com.mospolytech.data.schedule.scheduleDataModule
import com.mospolytech.domain.auth.authDomainModule
import com.mospolytech.domain.peoples.peoplesDomainModule
import com.mospolytech.domain.schedule.scheduleDomainModule
import com.mospolytech.domain.services.applications.applicationsDomainModule
import com.mospolytech.domain.services.payments.paymentsDomainModule
import com.mospolytech.domain.services.performance.performanceDomainModule
import com.mospolytech.domain.services.personal.personalDomainModule
import com.mospolytech.features.base.BaseFeatureModule
import com.mospolytech.features.peoples.peoplesFeaturesModule
import com.mospolytech.features.schedule.scheduleFeatureModule
import com.mospolytech.features.services.applications.applicationsFeatureModule
import com.mospolytech.features.services.payments.paymentsFeatureModule
import com.mospolytech.features.services.performance.performanceFeaturesModule
import com.mospolytech.features.services.personal.personalFeaturesModule
import com.mospolyteh.data.services.applications.applicationsDataModule
import com.mospolyteh.data.services.payments.paymentsDataModule
import com.mospolyteh.data.services.performance.performanceDataModule
import com.mospolyteh.data.services.personal.personalDataModule

val appModules =
    listOf(
        baseDataModule,
        BaseFeatureModule.di,
        authDataModule,
        authDomainModule,
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
