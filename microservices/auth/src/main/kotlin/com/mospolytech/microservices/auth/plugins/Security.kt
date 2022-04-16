package com.mospolytech.microservices.auth.plugins

import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.MpuPrincipal
import com.mospolytech.features.base.mpuAuth
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureSecurity() {
    
    install(Authentication) {
//        oauth("auth-oauth-google") {
//            urlProvider = { "http://localhost:8008/callback" }
//            providerLookup = {
//                OAuthServerSettings.OAuth2ServerSettings(
//                    name = "google",
//                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
//                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
//                    requestMethod = HttpMethod.Post,
//                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
//                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
//                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
//                )
//            }
//            client = HttpClient(Apache)
//        }
        mpuAuth(AuthConfigs.Mpu) {
            validate {
                MpuPrincipal(it.token)
            }
        }
    }
}
