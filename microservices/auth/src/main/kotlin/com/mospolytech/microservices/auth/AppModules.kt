package com.mospolytech.microservices.auth

import com.mospolytech.data.auth.authDataModule
import com.mospolytech.data.base.baseDataModule

val appModules = listOf(
    baseDataModule,
    authDataModule
)