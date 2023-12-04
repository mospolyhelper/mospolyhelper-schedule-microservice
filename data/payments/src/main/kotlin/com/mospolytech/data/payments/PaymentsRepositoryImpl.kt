package com.mospolytech.data.payments

import com.mospolytech.data.payments.response.toModel
import com.mospolytech.domain.services.payments.Contract
import com.mospolytech.domain.services.payments.PaymentsRepository

class PaymentsRepositoryImpl(private val service: PaymentsService) : PaymentsRepository {
    override suspend fun getPayments(token: String): Result<List<Contract>> {
        return runCatching {
            val paymentsResponse = service.getPayments(token).contracts
            paymentsResponse.toModel()
        }
    }
}
