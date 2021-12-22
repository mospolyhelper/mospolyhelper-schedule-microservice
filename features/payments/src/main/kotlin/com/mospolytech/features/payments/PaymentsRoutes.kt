package com.mospolytech.features.payments

import com.mospolytech.domain.payments.model.PaymentType
import com.mospolytech.domain.payments.repository.PaymentsRepository
import com.mospolytech.features.base.toResponse
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.paymentsRoutesV1(repository: PaymentsRepository) {
    routing {
        route("/payments") {
            get {
                call.respond(repository.getPayments())
            }
            route("/types") {
                get {
                    call.respond(repository.getPaymentTypes().map { it.name.lowercase() })
                }
            }
        }
    }
    routing {
        route("/payment") {
            get<PaymentsTypeRequest> {
                call.respond(repository.getPayment(it.type))
            }
        }
    }
}

@Location("/{type}")
data class PaymentsTypeRequest(
    val type: PaymentType
)