package com.mospolytech.domain.payments.repository

import com.mospolytech.domain.payments.model.Contracts
import com.mospolytech.domain.payments.model.Payments
import com.mospolytech.domain.payments.model.PaymentType

interface PaymentsRepository {
    suspend fun getPaymentTypes(token: String): Result<List<PaymentType>>
    suspend fun getPayments(token: String, paymentType: PaymentType? = null): Result<Contracts>
}