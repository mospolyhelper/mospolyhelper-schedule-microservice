package com.mospolytech.data.personal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class PersonalService(
    private val client: HttpClient
) {
    companion object {
        private const val BaseUrl = "https://e.mospolytech.ru"
        private const val ApiUrl = "$BaseUrl/old/lk_api.php"

        private const val GetPersonal = "$ApiUrl?getUser="
    }

    suspend fun getPersonalInfo(token: String): PersonalResponse {
        return client.get(GetPersonal) {
            parameter("token", token)
        }.body()
    }
}