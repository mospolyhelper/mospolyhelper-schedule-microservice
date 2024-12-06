package com.mospolytech.data.base

import io.ktor.client.*
import io.ktor.client.engine.apache5.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.apache.hc.client5.http.ssl.TrustAllStrategy
import org.apache.hc.core5.ssl.SSLContextBuilder
import org.koin.dsl.module

val baseDataModule =
    module {
        single {
            HttpClient(Apache5) {
                install(ContentNegotiation) {
                    val json =
                        Json {
                            ignoreUnknownKeys = true

                            // Api политеха может выдать и "id": 123, и "id": "123"
                            isLenient = true
                        }

                    json(json)
                    json(json, ContentType.Text.Any)
                }

                install(Logging) {
                    logger = CustomLogger
                    level = LogLevel.INFO
                }

                engine {
                    customizeClient {
                        sslContext = SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build()
                        socketTimeout = 10_000_000
                        connectTimeout = 0
                    }
                }
            }
        }
    }

private object CustomLogger : Logger {
    override fun log(message: String) {
        // Replace the token parameter in URLs with asterisks
        val filteredMessage = message.replace(Regex("token=[^&\\s]*"), "token=****")
        Logger.DEFAULT.log(filteredMessage)
    }
}
