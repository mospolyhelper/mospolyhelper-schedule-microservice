package com.mospolytech.domain.auth

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authDomainModule =
    module {
        singleOf(::GetJwtTokenUseCase)
        singleOf(::GetJwtRefreshTokenUseCase)
        singleOf(::ParseRefreshTokenUseCase)
    }
