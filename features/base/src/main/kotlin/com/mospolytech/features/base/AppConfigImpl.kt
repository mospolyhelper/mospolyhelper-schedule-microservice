package com.mospolytech.features.base

import com.mospolytech.domain.base.AppConfig
import io.ktor.server.config.*

class AppConfigImpl(private val config: ApplicationConfig) : AppConfig {
    override val url: String by lazy { config.propertyOrNull("postgres.url")?.getString().orEmpty() }
    override val driver: String by lazy { config.propertyOrNull("postgres.driver")?.getString().orEmpty() }
    override val login: String by lazy { config.propertyOrNull("postgres.login")?.getString().orEmpty() }
    override val password: String by lazy { config.propertyOrNull("postgres.password")?.getString().orEmpty() }
}
