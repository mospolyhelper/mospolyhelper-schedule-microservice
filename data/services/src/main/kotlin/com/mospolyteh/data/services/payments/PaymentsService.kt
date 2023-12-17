package com.mospolyteh.data.services.payments

import com.mospolyteh.data.services.payments.response.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class PaymentsService(
    private val client: HttpClient,
) {
    companion object {
        private const val BASE_URL = "https://e.mospolytech.ru"
        private const val API_URL = "$BASE_URL/old/lk_api.php"

        private const val GET_PAYMENTS = "$API_URL?getPayments="
    }

    suspend fun getPayments(token: String): Response {
        return client.get(GET_PAYMENTS) {
            parameter("token", token)
        }.body()
    }
}
