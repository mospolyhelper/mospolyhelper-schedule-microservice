package com.mospolytech.data.peoples

import com.mospolytech.domain.peoples.repository.PeoplesRepository
import org.koin.dsl.module

val peoplesDataModule = module {
    single<PeoplesRepository> { PeoplesRepositoryImpl() }
}