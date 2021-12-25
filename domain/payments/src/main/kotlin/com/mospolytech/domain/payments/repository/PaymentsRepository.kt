package com.mospolytech.domain.payments.repository

import com.mospolytech.domain.payments.model.PaymentType
import com.mospolytech.domain.payments.model.Payments

interface PaymentsRepository {
    fun getPaymentTypes(): List<PaymentType>
    fun getPayment(type: PaymentType): Payments
    fun getPayments(): List<Payments>
}