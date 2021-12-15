package com.mospolytech.microservices.account

import com.mospolytech.data.applications.applicationsDataModule
import com.mospolytech.data.base.baseDataModule
import com.mospolytech.data.payments.paymentsDataModule
import com.mospolytech.domain.applications.applicationsDomainModule
import com.mospolytech.domain.payments.paymentsDomainModule
import com.mospolytech.features.applications.applicationsFeatureModule
import com.mospolytech.features.payments.paymentsFeatureModule

val appModules = listOf(
    baseDataModule,
    applicationsDataModule,
    applicationsDomainModule,
    applicationsFeatureModule,
    paymentsDataModule,
    paymentsDomainModule,
    paymentsFeatureModule
)