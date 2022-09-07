package com.mospolytech.data.peoples

import com.mospolytech.data.peoples.remote.StudentsRemoteDS
import com.mospolytech.data.peoples.remote.TeachersRemoteDS
import com.mospolytech.data.peoples.repository.StudentsRepositoryImpl
import com.mospolytech.data.peoples.repository.TeachersRepositoryImpl
import com.mospolytech.data.peoples.service.StudentsService
import com.mospolytech.data.peoples.service.TeachersService
import com.mospolytech.domain.peoples.repository.StudentsRepository
import com.mospolytech.domain.peoples.repository.TeachersRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val peoplesDataModule = module {
    singleOf(::TeachersService)
    singleOf(::StudentsService)
    singleOf(::StudentsRemoteDS)
    singleOf(::TeachersRemoteDS)
    singleOf(::StudentsRepositoryImpl) { bind<StudentsRepository>() }
    singleOf(::TeachersRepositoryImpl) { bind<TeachersRepository>() }
}
