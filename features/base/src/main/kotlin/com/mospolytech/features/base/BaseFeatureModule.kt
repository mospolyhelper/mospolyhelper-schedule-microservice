package com.mospolytech.features.base

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object BaseFeatureModule {
    val di = module {
        singleOf(::AppConfigImpl)
        singleOf(::JobSchedulerManager)
    }
}
