package com.mospolytech.data.base

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustAllStrategy
import org.apache.http.ssl.SSLContextBuilder
import org.koin.dsl.module

val baseDataModule =
    module {
        single {
            HttpClient(Apache) {
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
                        setSSLContext(SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                        setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
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
