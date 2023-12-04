package com.mospolytech.domain.services.payments

interface PaymentsRepository {
    suspend fun getPayments(token: String): Result<List<Contract>>
}
