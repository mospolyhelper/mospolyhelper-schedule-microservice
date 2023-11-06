package com.mospolytech.domain.base

interface AppConfig {
    val url: String
    val driver: String
    val login: String
    val password: String

    val adminKey: String

    val getStudentsUrl: String
    val getStudentsAuth: String

    val getStaffUrl: String
    val getStaffAuth: String

    val mainLkLogin: String
    val mainLkPassword: String
}
