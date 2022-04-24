package com.mospolytech.data.auth

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class AuthService(
    private val client: HttpClient
) {
    companion object {
        private const val BaseUrl = "https://e.mospolytech.ru"
        private const val ApiUrl = "$BaseUrl/old/lk_api.php"

        private const val GetToken = ApiUrl
    }

    suspend fun getToken(login: String, password: String): TokenResponse {
        return client.submitForm(
            GetToken,
            Parameters.build {
                append("ulogin", login)
                append("upassword", password)
            }
        ).body()
    }
}