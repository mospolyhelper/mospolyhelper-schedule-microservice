package com.mospolyteh.data.services.applications

import com.mospolytech.domain.services.applications.ApplicationsRepository
import org.koin.dsl.module

val applicationsDataModule =
    module {
        single<ApplicationsRepository> { ApplicationsRepositoryImpl() }
    }
