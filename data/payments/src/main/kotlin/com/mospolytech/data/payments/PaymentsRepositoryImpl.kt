package com.mospolytech.data.payments

import com.mospolytech.data.payments.response.toModel
import com.mospolytech.domain.payments.model.Contracts
import com.mospolytech.domain.payments.model.PaymentType
import com.mospolytech.domain.payments.model.PaymentType.Dormitory
import com.mospolytech.domain.payments.model.PaymentType.Education
import com.mospolytech.domain.payments.repository.PaymentsRepository

class PaymentsRepositoryImpl(private val service: PaymentsService): PaymentsRepository {

    override suspend fun getPaymentTypes(token: String): Result<List<PaymentType>> {
        return runCatching {
            val paymentsResponse = service.getPayments(token)
            mutableListOf<PaymentType>().apply {
                if (paymentsResponse.dormitory.isNotEmpty()) add(Dormitory)
                if (paymentsResponse.education.isNotEmpty()) add(Education)
            }
        }
    }

    override suspend fun getPayments(token: String, paymentType: PaymentType?): Result<Contracts> {
        return runCatching {
            val paymentsResponse = service.getPayments(token)
            paymentsResponse.toModel()
        }
    }
}