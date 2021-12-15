package com.mospolytech.data.payments

import com.mospolytech.domain.payments.model.Payment
import com.mospolytech.domain.payments.model.PaymentType
import com.mospolytech.domain.payments.model.PaymentType.*
import com.mospolytech.domain.payments.model.Payments
import com.mospolytech.domain.payments.repository.PaymentsRepository
import java.time.LocalDate

class PaymentsRepositoryImpl: PaymentsRepository {

    override fun getPaymentTypes(): List<PaymentType> {
        return listOf(Dormitory, Education)
    }

    override fun getPayment(type: PaymentType): Payments {
        return when(type) {
            Dormitory -> Payments("1488", LocalDate.now(), 1488228f, -650f, LocalDate.now(), "Руб", "121212121212", List(5) { Payment(LocalDate.now(), 228f) }, null)
            Education -> Payments("228", LocalDate.now(), 2281488f, 650f, LocalDate.now(), "Руб", "121212121212", List(5) { Payment(LocalDate.now(), 1499f) }, null)
        }
    }

    override fun getPayments(): List<Payments> {
        return listOf( Payments("1488", LocalDate.now(), 1488228f, -650f, LocalDate.now(), "Руб", "121212121212", List(5) { Payment(LocalDate.now(), 228f) }, Dormitory),
            Payments("228", LocalDate.now(), 2281488f, 650f, LocalDate.now(), "Руб", "121212121212", List(5) { Payment(LocalDate.now(), 1499f) }, Education))
    }
}