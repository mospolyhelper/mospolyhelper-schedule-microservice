package com.mospolyteh.data.services.personal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class PersonalService(
    private val client: HttpClient,
) {
    companion object {
        private const val BASE_URL = "https://e.mospolytech.ru"
        private const val API_URL = "$BASE_URL/old/lk_api.php"

        private const val GET_PERSONAL = "$API_URL?getUser="
    }

    suspend fun getPersonalInfo(token: String): PersonalResponse {
        return client.get(GET_PERSONAL) {
            parameter("token", token)
        }.body()
    }
}
