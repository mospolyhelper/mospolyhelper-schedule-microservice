package com.mospolytech.domain.auth

data class RefreshTokenModel(
    val token: String,
    val guid: String?,
    val jwtRefresh: String?,
)
