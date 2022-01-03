package com.mospolytech.data.base

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustAllStrategy
import org.apache.http.ssl.SSLContextBuilder
import org.koin.dsl.module

val baseDataModule = module {
    single {
        HttpClient(Apache) {
            install(ContentNegotiation) {
                val json = Json {
                    ignoreUnknownKeys = true
                }

                json(json)
                json(json, ContentType.Text.Any)
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            engine {
                customizeClient {
                    setSSLContext(SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                }
            }
        }
    }
}