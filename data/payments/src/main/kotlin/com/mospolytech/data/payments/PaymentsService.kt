package com.mospolytech.data.payments

import com.mospolytech.data.payments.response.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class PaymentsService(
    private val client: HttpClient
) {
    companion object {
        private const val BaseUrl = "https://e.mospolytech.ru"
        private const val ApiUrl = "$BaseUrl/old/lk_api.php"

        private const val GetPayments = "$ApiUrl?getPayments="
    }

    suspend fun getPayments(token: String): Response {
        return client.get(GetPayments) {
            parameter("token", token)
        }.body()
    }
}
