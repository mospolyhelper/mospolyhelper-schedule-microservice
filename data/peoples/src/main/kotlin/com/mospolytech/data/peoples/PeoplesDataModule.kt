package com.mospolytech.data.peoples

import com.mospolytech.domain.peoples.repository.PeoplesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.binds
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val peoplesDataModule = module {
    singleOf(::TeachersService)
    singleOf(::PeoplesRepositoryImpl) { bind<PeoplesRepository>() }
}