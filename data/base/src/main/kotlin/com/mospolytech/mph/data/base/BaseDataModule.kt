package com.mospolytech.mph.data.base

import io.ktor.client.*
import org.koin.dsl.module

val baseDataModule = module {
    single { HttpClient() }
}