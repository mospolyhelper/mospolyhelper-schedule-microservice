package com.mospolytech.data.applications

import com.mospolytech.domain.applications.repository.ApplicationsRepository
import org.koin.dsl.module

val applicationsDataModule =
    module {
        single<ApplicationsRepository> { ApplicationsRepositoryImpl() }
    }
