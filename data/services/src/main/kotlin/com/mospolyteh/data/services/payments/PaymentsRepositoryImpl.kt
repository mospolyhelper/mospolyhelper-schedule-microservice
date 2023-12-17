package com.mospolyteh.data.services.payments

import com.mospolytech.domain.services.payments.Contract
import com.mospolytech.domain.services.payments.PaymentsRepository
import com.mospolyteh.data.services.payments.response.toModel

class PaymentsRepositoryImpl(private val service: PaymentsService) : PaymentsRepository {
    override suspend fun getPayments(token: String): Result<List<Contract>> {
        return runCatching {
            val paymentsResponse = service.getPayments(token).contracts
            paymentsResponse.toModel()
        }
    }
}
