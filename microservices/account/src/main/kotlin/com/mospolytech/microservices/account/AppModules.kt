package com.mospolytech.microservices.account

import com.mospolytech.data.applications.applicationsDataModule
import com.mospolytech.data.base.baseDataModule
import com.mospolytech.data.payments.paymentsDataModule
import com.mospolytech.data.personal.personalDataModule
import com.mospolytech.domain.applications.applicationsDomainModule
import com.mospolytech.domain.payments.paymentsDomainModule
import com.mospolytech.domain.personal.personalDomainModule
import com.mospolytech.features.applications.applicationsFeatureModule
import com.mospolytech.features.payments.paymentsFeatureModule
import com.mospolytech.features.personal.personalFeaturesModule

val appModules = listOf(
    baseDataModule,
    applicationsDataModule,
    applicationsDomainModule,
    applicationsFeatureModule,
    paymentsDataModule,
    paymentsDomainModule,
    paymentsFeatureModule,
    personalDataModule,
    personalDomainModule,
    personalFeaturesModule
)