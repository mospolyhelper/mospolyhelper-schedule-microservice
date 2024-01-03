package com.mospolytech.domain.services.payments

interface PaymentsRepository {
    suspend fun getPayments(
        id: String?,
        token: String,
    ): Result<PaymentsApi>
}
