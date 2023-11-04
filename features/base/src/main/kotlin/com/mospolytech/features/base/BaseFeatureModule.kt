package com.mospolytech.features.base

import com.mospolytech.domain.base.AppConfig
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object BaseFeatureModule {
    val di =
        module {
            singleOf(::AppConfigImpl) { bind<AppConfig>() }
            singleOf(::JobSchedulerManager)
        }
}
