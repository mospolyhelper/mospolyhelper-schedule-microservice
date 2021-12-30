package com.mospolytech.microservices.auth.plugins

import com.auth0.jwk.*
import com.auth0.jwt.*
import com.auth0.jwt.exceptions.*
import com.auth0.jwt.impl.*
import com.auth0.jwt.interfaces.*
import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import org.slf4j.*
import java.util.*
import kotlin.reflect.*

private val CustomAuthKey: Any = "JWTAuth"

private val CustomLogger: Logger = LoggerFactory.getLogger("com.mospolytech.microservices.auth.plugins")

/**
 * Represents a JWT credential consist of the specified [payload]
 * @param payload JWT
 * @see Payload
 */
class CustomAuthCredential(token: String) : Credential

/**
 * Represents a JWT principal consist of the specified [String]
 */
class CustomAuthPrincipal(token: String) : Principal

/**
 * JWT authentication provider that will be registered with the specified [name]
 */
class JWTAuthenticationProvider(config: Configuration) : AuthenticationProvider(config) {
    internal val authHeader: (ApplicationCall) -> HttpAuthHeader? = config.authHeader
    internal val authenticationFunction = config.authenticationFunction
    internal val challengeFunction: CustomAuthChallengeFunction = config.challenge

    /**
     * JWT auth provider configuration
     */
    class Configuration(name: String?) : AuthenticationProvider.Configuration(name) {
        internal var authenticationFunction: AuthenticationFunction<CustomAuthCredential> = {
            throw NotImplementedError(
                "JWT auth validate function is not specified. Use jwt { validate { ... } } to fix."
            )
        }

        internal var authHeader: (ApplicationCall) -> HttpAuthHeader? =
            { call -> call.request.parseAuthorizationHeaderOrNull() }

        internal var challenge: CustomAuthChallengeFunction = { scheme ->
            call.respond(
                UnauthorizedResponse(
                    HttpAuthHeader.Parameterized(
                        scheme,
                        mapOf(HttpAuthHeader.Parameters.Realm to "realm")
                    )
                )
            )
        }
        internal fun build() = JWTAuthenticationProvider(this)
    }
}

fun Authentication.Configuration.customAuth(
    name: String? = null,
    configure: JWTAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = JWTAuthenticationProvider.Configuration(name).apply(configure).build()
    val authenticate = provider.authenticationFunction

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val token = provider.authHeader(call)
        if (token == null) {
            context.bearerChallenge(AuthenticationFailedCause.NoCredentials, provider.challengeFunction)
            return@intercept
        }

        try {
            val principal = verifyAndValidate(call, token) {
                authenticate(this, CustomAuthCredential(token.getBlob()!!))
            }
            if (principal != null) {
                context.principal(principal)
                return@intercept
            }

            context.bearerChallenge(
                AuthenticationFailedCause.InvalidCredentials,
                provider.challengeFunction
            )
        } catch (cause: Throwable) {
            val message = cause.message ?: cause.javaClass.simpleName
            CustomLogger.trace("JWT verification failed: {}", message)
            context.error(CustomAuthKey, AuthenticationFailedCause.Error(message))
        }
    }
    register(provider)
}

/**
 * Specifies what to send back if session authentication fails.
 */
typealias CustomAuthChallengeFunction =
        suspend PipelineContext<*, ApplicationCall>.(defaultScheme: String) -> Unit

private fun AuthenticationContext.bearerChallenge(
    cause: AuthenticationFailedCause,
    challengeFunction: CustomAuthChallengeFunction
) {
    challenge(CustomAuthKey, cause) {
        challengeFunction(this, "bearer")
        if (!it.completed && call.response.status() != null) {
            it.complete()
        }
    }
}

private suspend fun verifyAndValidate(
    call: ApplicationCall,
    token: HttpAuthHeader,
    validate: suspend ApplicationCall.(String) -> Principal?
): Principal? {
    val jwt = try {
        if (token.toString() == "qwerqw") token.getBlob() else null
    } catch (ex: JWTVerificationException) {
        CustomLogger.trace("Token verification failed: {}", ex.message)
        null
    } ?: return null
    return validate(call, jwt)
}

private fun HttpAuthHeader.getBlob() = when {
    this is HttpAuthHeader.Single && authScheme == "bearer" -> blob
    else -> null
}

private fun ApplicationRequest.parseAuthorizationHeaderOrNull() = try {
    parseAuthorizationHeader()
} catch (ex: IllegalArgumentException) {
    CustomLogger.trace("Illegal HTTP auth header", ex)
    null
}