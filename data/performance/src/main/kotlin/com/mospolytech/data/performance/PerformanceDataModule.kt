package com.mospolytech.data.performance

import com.mospolytech.domain.perfomance.repository.PerformanceRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val performanceDataModule =
    module {
        singleOf(::PerformanceRepositoryImpl) { bind<PerformanceRepository>() }
        single { PerformanceService(get()) }
    }
