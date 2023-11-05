package com.mospolytech.data.auth

import com.mospolytech.domain.base.exception.AuthenticationException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class AuthService(
    private val client: HttpClient,
) {
    companion object {
        private const val BASE_URL = "https://e.mospolytech.ru"
        private const val API_URL = "$BASE_URL/old/lk_api.php"

        private const val GET_TOKEN = API_URL

        private const val WRONG_CREDENTIALS_CODE = 400
    }

    suspend fun getToken(
        login: String,
        password: String,
    ): TokenResponse {
        return client.submitForm(
            GET_TOKEN,
            Parameters.build {
                append("ulogin", login)
                append("upassword", password)
            },
        ).also {
            if (it.status.value == WRONG_CREDENTIALS_CODE) {
                throw AuthenticationException()
            }
        }.body()
    }
}
