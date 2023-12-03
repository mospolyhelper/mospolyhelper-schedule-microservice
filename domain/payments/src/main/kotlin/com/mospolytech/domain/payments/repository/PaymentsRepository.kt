package com.mospolytech.domain.payments.repository

import com.mospolytech.domain.payments.model.Contract
import com.mospolytech.domain.payments.model.PaymentType

interface PaymentsRepository {
    suspend fun getPaymentTypes(token: String): Result<List<PaymentType>>

    suspend fun getPayments(token: String): Result<List<Contract>>
}
