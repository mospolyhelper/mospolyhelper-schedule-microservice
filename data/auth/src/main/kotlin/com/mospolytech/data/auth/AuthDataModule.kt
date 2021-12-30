package com.mospolytech.data.auth

import com.mospolytech.domain.auth.AuthRepository
import org.koin.dsl.module

val authDataModule = module {
    single { AuthService(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}