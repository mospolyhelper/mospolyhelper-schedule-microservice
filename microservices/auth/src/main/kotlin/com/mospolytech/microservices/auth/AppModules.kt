package com.mospolytech.microservices.auth

import com.mospolytech.data.auth.authDataModule
import com.mospolytech.data.base.baseDataModule
import com.mospolytech.data.personal.personalDataModule
import com.mospolytech.domain.personal.personalDomainModule

val appModules = listOf(
    baseDataModule,
    authDataModule,
    personalDomainModule,
    personalDataModule
)
