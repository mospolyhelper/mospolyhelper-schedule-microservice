package com.mospolytech.data.payments

import com.mospolytech.domain.payments.repository.PaymentsRepository
import org.koin.dsl.module

val paymentsDataModule = module {
    single<PaymentsRepository> { PaymentsRepositoryImpl() }
}