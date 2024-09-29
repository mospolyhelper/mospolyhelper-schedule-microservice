package com.mospolytech.domain.auth

data class TokenModel(
    val token: String,
    val guid: String?,
    val jwt: String?,
    val jwtRefresh: String?,
)
