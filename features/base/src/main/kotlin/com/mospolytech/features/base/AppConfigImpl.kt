package com.mospolytech.features.base

import com.mospolytech.domain.base.AppConfig
import com.mospolytech.domain.base.model.AdminLogLevel
import io.ktor.server.config.*

class AppConfigImpl(private val config: ApplicationConfig) : AppConfig {
    override val url: String by lazy { config.propertyOrNull("postgres.url")?.getString().orEmpty() }
    override val driver: String by lazy { config.propertyOrNull("postgres.driver")?.getString().orEmpty() }
    override val login: String by lazy { config.propertyOrNull("postgres.login")?.getString().orEmpty() }
    override val password: String by lazy { config.propertyOrNull("postgres.password")?.getString().orEmpty() }

    override val adminKey: String by lazy {
        config.propertyOrNull("admin.key")?.getString().orEmpty()
    }

    override val adminLogLevel: AdminLogLevel by lazy {
        val level = config.propertyOrNull("admin.logLevel")?.getString().orEmpty()
        when (level) {
            "FULL" -> AdminLogLevel.FULL
            else -> AdminLogLevel.MIN
        }
    }

    override val getStudentsUrl: String by lazy {
        config.propertyOrNull("urls.getStudents")?.getString().orEmpty()
    }
    override val getStudentsAuth: String by lazy {
        config.propertyOrNull("auth.getStudents")?.getString().orEmpty()
    }

    override val getStaffUrl: String by lazy {
        config.propertyOrNull("urls.getStaff")?.getString().orEmpty()
    }
    override val getStaffAuth: String by lazy {
        config.propertyOrNull("auth.getStaff")?.getString().orEmpty()
    }

    override val mainLkLogin: String by lazy {
        config.propertyOrNull("mainlk.login")?.getString().orEmpty()
    }

    override val mainLkPassword: String by lazy {
        config.propertyOrNull("mainlk.password")?.getString().orEmpty()
    }
}
