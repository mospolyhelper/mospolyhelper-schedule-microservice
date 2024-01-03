package com.mospolyteh.data.services.payments

import com.mospolytech.domain.services.payments.PaymentsApi
import com.mospolytech.domain.services.payments.PaymentsRepository
import com.mospolyteh.data.services.payments.response.toModel

class PaymentsRepositoryImpl(private val service: PaymentsService) : PaymentsRepository {
    override suspend fun getPayments(
        id: String?,
        token: String,
    ): Result<PaymentsApi> {
        return runCatching {
            val paymentsResponse = service.getPayments(token).contracts
            paymentsResponse.toModel(id)
        }
    }
}
