package com.mospolyteh.data.services.payments

import com.mospolytech.domain.services.payments.PaymentsRepository
import org.koin.dsl.module

val paymentsDataModule =
    module {
        single<PaymentsRepository> { PaymentsRepositoryImpl(get()) }
        single { PaymentsService(get()) }
    }
