package com.mospolytech.features.payments

import com.mospolytech.domain.payments.model.PaymentType
import com.mospolytech.domain.payments.repository.PaymentsRepository
import com.mospolytech.features.base.AuthConfigs
import com.mospolytech.features.base.utils.getTokenOrRespondError
import com.mospolytech.features.base.utils.respondResult
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.paymentsRoutesV1(repository: PaymentsRepository) {
    routing {
        authenticate(AuthConfigs.Mpu, optional = true) {
            route("/payments") {
                get<PaymentsTypeRequest> {
                    val token = call.getTokenOrRespondError() ?: return@get
                    call.respondResult(repository.getPayments(token, it.type))
                }
                get<NoPaymentsType> {
                    val token = call.getTokenOrRespondError() ?: return@get
                    call.respondResult(repository.getPayments(token))
                }
                route("/types") {
                    get {
                        val token = call.getTokenOrRespondError() ?: return@get
                        call.respondResult(repository.getPaymentTypes(token))
                    }
                }
            }
        }
    }
}

@Location("/{type}")
data class PaymentsTypeRequest(
    val type: PaymentType
)
@Location("/")
object NoPaymentsType