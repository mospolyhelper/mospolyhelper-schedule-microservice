package com.mospolytech.features.base

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.*
import com.auth0.jwt.impl.*
import com.auth0.jwt.interfaces.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import org.slf4j.*

private val CustomAuthKey: Any = "MpuAuth"

private val CustomLogger: Logger = LoggerFactory.getLogger("com.mospolytech.microservices.auth.plugins")

/**
 * Represents a JWT credential consist of the specified [payload]
 * @param payload JWT
 * @see Payload
 */
class MpuCredential(val payload: Payload) : Credential

/**
 * Represents a JWT principal consist of the specified [String]
 */
class MpuPrincipal(val token: String) : Principal

/**
 * JWT authentication provider that will be registered with the specified [name]
 */
class MpuAuthenticationProvider(private val secret: String, config: Configuration) : AuthenticationProvider(config) {
    internal val authHeader: (ApplicationCall) -> HttpAuthHeader? = config.authHeader
    internal val authenticationFunction = config.authenticationFunction
    internal val challengeFunction: MpuAuthChallengeFunction = config.challenge

    /**
     * JWT auth provider configuration
     */
    class Configuration(private val secret: String, name: String?) : AuthenticationProvider.Config(name) {
        internal var authenticationFunction: AuthenticationFunction<MpuCredential> = {
            throw NotImplementedError(
                "JWT auth validate function is not specified. Use jwt { validate { ... } } to fix.",
            )
        }

        internal var authHeader: (ApplicationCall) -> HttpAuthHeader? =
            { call -> call.request.parseAuthorizationHeaderOrNull() }

        internal var challenge: MpuAuthChallengeFunction = { scheme ->
            call.respond(
                UnauthorizedResponse(
                    HttpAuthHeader.Parameterized(
                        scheme,
                        mapOf(HttpAuthHeader.Parameters.Realm to "realm"),
                    ),
                ),
            )
        }

        public fun validate(validate: suspend ApplicationCall.(MpuCredential) -> Principal?) {
            authenticationFunction = validate
        }

        internal fun build() = MpuAuthenticationProvider(secret, this)
    }

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val call = context.call

        val token = authHeader(call)
        if (token == null) {
            context.bearerChallenge(AuthenticationFailedCause.NoCredentials, challengeFunction)
            return
        }

        try {
            val principal = verifyAndValidate(secret, call, token) {
                authenticationFunction(this, MpuCredential(token.getBlob(secret)!!))
            }
            if (principal != null) {
                context.principal(principal)
                return
            }

            context.bearerChallenge(
                AuthenticationFailedCause.InvalidCredentials,
                challengeFunction,
            )
        } catch (cause: Throwable) {
            val message = cause.message ?: cause.javaClass.simpleName
            CustomLogger.trace("JWT verification failed: {}", message)
            context.error(CustomAuthKey, AuthenticationFailedCause.Error(message))
        }
    }
}

fun AuthenticationConfig.mpuAuth(
    name: String?,
    jwtSecret: String,
    configure: MpuAuthenticationProvider.Configuration.() -> Unit,
) {
    val provider = MpuAuthenticationProvider.Configuration(jwtSecret, name).apply(configure).build()
    register(provider)
}

/**
 * Specifies what to send back if session authentication fails.
 */
typealias MpuAuthChallengeFunction =
suspend MpuAuthChallengeContext.(defaultScheme: String) -> Unit

class MpuAuthChallengeContext(
    val call: ApplicationCall,
)

private fun AuthenticationContext.bearerChallenge(
    cause: AuthenticationFailedCause,
    challengeFunction: MpuAuthChallengeFunction,
) {
    challenge(CustomAuthKey, cause) { challenge, call ->
        challengeFunction(MpuAuthChallengeContext(call), "Bearer")
        if (!challenge.completed && call.response.status() != null) {
            challenge.complete()
        }
    }
}

private suspend fun verifyAndValidate(
    secret: String,
    call: ApplicationCall,
    token: HttpAuthHeader,
    validate: suspend ApplicationCall.(Payload) -> Principal?,
): Principal? {
    val jwt = try {
        token.getBlob(secret)
    } catch (ex: JWTVerificationException) {
        CustomLogger.trace("Token verification failed: {}", ex.message)
        null
    } ?: return null
    return validate(call, jwt)
}

private fun HttpAuthHeader.getBlob(secret: String) = when {
    this is HttpAuthHeader.Single && authScheme == "Bearer" -> {
        blob.decodeJwtToken(secret).parse()
    }
    else -> null
}

private fun String.decodeJwtToken(secret: String) = JWT
    .require(Algorithm.HMAC256(secret))
    .build()
    .verify(this)

private fun DecodedJWT.parse() =
    payload
        .decodeBase64String()
        .let { JWTParser().parsePayload(it) }

private fun ApplicationRequest.parseAuthorizationHeaderOrNull() = try {
    parseAuthorizationHeader()
} catch (ex: IllegalArgumentException) {
    CustomLogger.trace("Illegal HTTP auth header", ex)
    null
}
