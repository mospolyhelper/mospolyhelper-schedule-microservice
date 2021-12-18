package com.mospolytech.data.performance

import com.mospolytech.domain.perfomance.repository.PerformanceRepository
import org.koin.dsl.module

val performanceDataModule = module {
    single<PerformanceRepository> { PerformanceRepositoryImpl() }
}