package com.mospolytech.features.peoples

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val peoplesFeaturesModule =
    module {
        singleOf(::StudentsJobLauncher)
        singleOf(::StudentsUpdateJob)
        singleOf(::TeachersJobLauncher)
        singleOf(::TeachersUpdateJob)
    }
