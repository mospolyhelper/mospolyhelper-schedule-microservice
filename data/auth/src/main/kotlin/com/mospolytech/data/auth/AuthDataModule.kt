package com.mospolytech.data.auth

import com.mospolytech.domain.auth.AuthRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authDataModule =
    module {
        singleOf(::AuthService)
        singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
        singleOf(::AccountsDtoMapper)
    }
