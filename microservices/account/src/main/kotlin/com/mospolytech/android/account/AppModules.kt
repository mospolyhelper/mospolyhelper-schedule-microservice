package com.mospolytech.android.account

import com.mospolytech.data.applications.applicationsDataModule
import com.mospolytech.data.base.baseDataModule
import com.mospolytech.domain.applications.applicationsDomainModule
import com.mospolytech.features.applications.applicationsFeatureModule

val appModules = listOf(
    baseDataModule,
    applicationsDataModule,
    applicationsDomainModule,
    applicationsFeatureModule
)