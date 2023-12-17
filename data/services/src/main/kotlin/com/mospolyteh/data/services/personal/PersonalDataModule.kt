package com.mospolyteh.data.services.personal

import com.mospolytech.domain.services.personal.PersonalRepository
import org.koin.dsl.module

val personalDataModule =
    module {
        single { PersonalService(get()) }
        single<PersonalRepository> { PersonalRepositoryImpl(get()) }
    }
