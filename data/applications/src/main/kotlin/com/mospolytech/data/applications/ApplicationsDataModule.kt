package com.mospolytech.data.applications

import com.mospolytech.domain.services.applications.ApplicationsRepository
import org.koin.dsl.module

val applicationsDataModule =
    module {
        single<ApplicationsRepository> { ApplicationsRepositoryImpl() }
    }
