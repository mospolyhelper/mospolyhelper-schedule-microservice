package com.mospolytech.features.base

import com.mospolytech.domain.base.exception.InvalidCredentialsException
import com.mospolytech.domain.base.model.ApiError
import com.mospolytech.domain.base.model.ApiErrorResponse
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException

suspend fun ApplicationCall.respondError(exception: Throwable) {
    val status: HttpStatusCode
    val apiError: ApiErrorResponse

    when (exception) {
        is ClientRequestException ->
            when (exception.response.status) {
                HttpStatusCode.BadRequest -> {
                    status = HttpStatusCode.Forbidden
                    apiError = handleUnknownMicroserviceError(exception.response)
                }
                HttpStatusCode.Unauthorized -> {
                    status = HttpStatusCode.Unauthorized
                    apiError = handleTokenExpired()
                }
                else -> {
                    status = HttpStatusCode.BadGateway
                    apiError = apiError(status, "")
                }
            }
        is SerializationException -> {
            status = HttpStatusCode.InternalServerError
            apiError = handleSerializationException(exception)
        }
        is JsonConvertException -> {
            status = HttpStatusCode.InternalServerError
            apiError = handleSerializationException(exception)
        }
        is InvalidCredentialsException -> {
            status = HttpStatusCode.Unauthorized
            apiError = handleInvalidCredentials()
        }
        else -> {
            status = HttpStatusCode.BadGateway
            apiError = apiError(status, "")
        }
    }

    respond(status = status, message = apiError)
}

fun handleSerializationException(exception: SerializationException): ApiErrorResponse {
    val serviceName = "https://e.mospolytech.ru/old/lk_api.php"
    return apiError(
        code = "INTERNAL_SERIALIZATION_ERROR",
        message = "Ошибка при конвертации ответа от сервиса $serviceName",
        data = mapOf(
            "exceptionMessage" to exception.message.orEmpty(),
            "serviceName" to serviceName
        )
    )
}

fun handleSerializationException(exception: JsonConvertException): ApiErrorResponse {
    val serviceName = "https://e.mospolytech.ru/old/lk_api.php"
    return apiError(
        code = "INTERNAL_SERIALIZATION_ERROR",
        message = "Ошибка при конвертации ответа от сервиса $serviceName",
        data = mapOf(
            "exceptionMessage" to exception.message.orEmpty(),
            "serviceName" to serviceName
        )
    )
}

suspend fun handleUnknownMicroserviceError(response: HttpResponse): ApiErrorResponse {
    val serviceName = "https://e.mospolytech.ru/old/lk_api.php"
    return apiError(
        code = "UNKNOWN_SERVICE_ERROR",
        message = "Неизвестная ошибка при взаимодействии с сервисом. Попробуйте позже.",
        data = buildMap {
            put("service", serviceName)
            val body = response.bodyAsText()
            if (body.isNotEmpty()) {
                put("body", body)
            }
        }
    )
}

fun handleInvalidCredentials(): ApiErrorResponse {
    return apiError(
        code = "AUTH_INVALID_CREDENTIALS",
        message = "Неверный логин или пароль",
    )
}

fun handleTokenExpired(): ApiErrorResponse {
    return apiError(
        code = "TOKEN_EXPIRED",
        message = "Токен устарел. Пожалуйста, повторите аутентификацию.",
    )
}

private fun apiError(code: String, message: String, data: Map<String, String>? = null): ApiErrorResponse {
    return ApiErrorResponse(
        errors = listOf(
            ApiError(
                code = code,
                message = message,
                data = data,
            ),
        ),
    )
}

private fun apiError(code: HttpStatusCode, message: String, data: Map<String, String>? = null): ApiErrorResponse {
    return apiError(code = code.value.toString(), message = message, data = data)
}
