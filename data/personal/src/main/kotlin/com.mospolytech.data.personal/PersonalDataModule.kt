package com.mospolytech.data.personal

import com.mospolytech.domain.personal.repository.PersonalRepository
import org.koin.dsl.module

val personalDataModule = module {
    single { PersonalService(get()) }
    single<PersonalRepository> { PersonalRepositoryImpl() }
}