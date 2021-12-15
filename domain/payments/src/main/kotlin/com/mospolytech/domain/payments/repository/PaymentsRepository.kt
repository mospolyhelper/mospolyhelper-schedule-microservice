package com.mospolytech.domain.payments.repository

import com.mospolytech.domain.payments.model.PaymentType
import com.mospolytech.domain.payments.model.Payments

interface PaymentsRepository {
    fun getPaymentTypes(): PaymentType
    fun getPayments(type: PaymentType): Payments
}