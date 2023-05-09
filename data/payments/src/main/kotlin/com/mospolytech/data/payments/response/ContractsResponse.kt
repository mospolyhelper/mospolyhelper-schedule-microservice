package com.mospolytech.data.payments.response

import com.mospolytech.domain.payments.model.Contracts
import com.mospolytech.domain.payments.model.Payment
import com.mospolytech.domain.payments.model.PaymentType
import com.mospolytech.domain.payments.model.Payments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Serializable
data class Response(
    val contracts: ContractsResponse,
)

@Serializable
data class ContractsResponse(
    val education: List<PaymentsResponse>,
    val dormitory: List<PaymentsResponse>,
)

@Serializable
data class PaymentsResponse(
    val id: String,
    val contragent: String,
    val student: String,
    val number: String,
    val name: String,
    val type: String,
    val level: String,
    @SerialName("dorm_num")
    val dormNum: String,
    @SerialName("dorm_room")
    val dormRoom: String,
    val file: String,
    val bill: String,
    @SerialName("can_sign")
    val canSign: Boolean,
    @SerialName("signed_user")
    val signedUser: Boolean,
    @SerialName("signed_user_date")
    val signedUserDate: String,
    @SerialName("signed_user_time")
    val signedUserTime: String,
    val startDate: String,
    val endDatePlan: String,
    val endDateFact: String,
    @SerialName("qr_current")
    val qrCurrent: String,
    @SerialName("qr_total")
    val qrTotal: String,
    val sum: String,
    val balance: String,
    @SerialName("balance_currdate")
    val balanceCurrdate: String,
    val lastPaymentDate: String,
    val payments: List<PaymentResponse>,
    val agreements: List<AgreementsResponse>,
    val paygraph: List<PaygraphResponse>,
)

@Serializable
data class PaymentResponse(
    val date: String,
    val value: String,
)

@Serializable
data class AgreementsResponse(
    val id: String,
    val name: String,
    val type: String,
    val date: String,
    val file: String,
    @SerialName("can_sign")
    val canSign: Boolean,
    @SerialName("signed_user")
    val signedUser: Boolean,
    @SerialName("signed_user_date")
    val signedUserDate: String,
    @SerialName("signed_user_time")
    val signedUserTime: String,
)

@Serializable
data class PaygraphResponse(
    val year: String,
    val semestr: String,
    @SerialName("date_plan")
    val datePlan: String,
    @SerialName("date_start")
    val dateStart: String,
    @SerialName("date_end")
    val dateEnd: String,
    @SerialName("sum_price")
    val sumPrice: String,
    val sum: String,
    @SerialName("sum_pay")
    val sumPay: String,
)

fun ContractsResponse.toModel(): Contracts {
    val payments = mutableMapOf<PaymentType, Payments>().apply {
        if (dormitory.isNotEmpty()) put(PaymentType.Dormitory, dormitory.map { it.toModel() }.first())
        if (education.isNotEmpty()) put(PaymentType.Education, education.map { it.toModel() }.first())
    }
    return Contracts(payments)
}

fun PaymentsResponse.toModel(): Payments {
    val lastPayment = try { lastPaymentDate.toDate() } catch (e: Throwable) { null }
    return Payments(
        id = id,
        student = student,
        number = number,
        name = name,
        type = type,
        level = level.ifEmpty { null },
        dormNum = dormNum.ifEmpty { null },
        dormRoom = dormRoom.ifEmpty { null },
        startDate = startDate.toDate(),
        endDate = endDateFact.ifEmpty { endDatePlan }.toDate(),
        qrCurrent = qrCurrent.ifEmpty { null },
        qrTotal = qrCurrent.ifEmpty { null },
        requestDate = LocalDate.now(),
        sum = sum,
        balance = balance,
        balanceCurrent = balanceCurrdate,
        lastPaymentDate = lastPayment,
        payments = payments.map { it.toModel() },
    )
}

fun PaymentResponse.toModel() = Payment(date.toDate(), value)

fun String.toDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("MMMM d',' yyyy", Locale.US))
}
