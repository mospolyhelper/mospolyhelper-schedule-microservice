package com.mospolyteh.data.services.payments.response

import com.mospolytech.domain.base.utils.Moscow
import com.mospolytech.domain.base.utils.decapitalized
import com.mospolytech.domain.base.utils.formatRoubles
import com.mospolytech.domain.services.payments.Contract
import com.mospolytech.domain.services.payments.Payment
import com.mospolytech.domain.services.payments.PaymentMethod
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.todayIn
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter
import java.util.*
import java.time.LocalDate as JavaLocalDate

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

fun ContractsResponse.toModel(): List<Contract> {
    val payments =
        buildList<Contract> {
            addAll(dormitory.map { it.toModel() })
            addAll(education.map { it.toModel() })
        }
    return payments
}

private const val PAYMENT_QR_DESCRIPTION = """Вы можете сделать скриншот экрана или скачать QR-код на устройство, затем открыть его в мобильном приложении вашего банка:
Оплата по QR-коду -> Загрузить изображение"""
private const val PAYMENT_QR_TITLE = "Оплата долга по QR"
private const val PAYMENT_QR_TITLE_ALL = "Оплата всей суммы по QR"

fun PaymentsResponse.toModel(): Contract {
    val paymentMethods =
        buildList {
            if (qrCurrent.isNotEmpty()) {
                add(
                    PaymentMethod(
                        type = PaymentMethod.URL_TYPE,
                        title = PAYMENT_QR_TITLE,
                        description = PAYMENT_QR_DESCRIPTION,
                        url = qrCurrent,
                    ),
                )
            }
            if (qrTotal.isNotEmpty()) {
                add(
                    PaymentMethod(
                        type = PaymentMethod.URL_TYPE,
                        title = PAYMENT_QR_TITLE_ALL,
                        description = PAYMENT_QR_DESCRIPTION,
                        url = qrTotal,
                    ),
                )
            }
        }

    val today = Clock.System.todayIn(TimeZone.Moscow)

    val balanceDecimal = fixBalance(balance).toBigDecimalOrNull()
    val isNegativeBalance = balanceDecimal?.let { it < 0.toBigDecimal() } ?: false
    val formattedBalance = balanceDecimal?.formatRoubles() ?: balance

    val replenishments = payments.map { it.toModel(title = type) }
    val contractPayments = paygraph.mapNotNull { it.toModel(title = type, today = today) }
    val allPayments = (replenishments + contractPayments).sortedByDescending { it.date }

    return Contract(
        id = id,
        title = "$type $number",
        description = name,
        startDate = startDate.toDate(),
        endDate = endDateFact.ifEmpty { endDatePlan }.toDate(),
        balance = formattedBalance,
        paymentMethods = paymentMethods,
        isNegativeBalance = isNegativeBalance,
        payments = allPayments,
    )
}

private fun fixBalance(balance: String): String {
    return when {
        balance.isEmpty() -> balance
        balance.first().isDigit().not() -> balance.takeLast(balance.length - 1)
        else -> "-$balance"
    }
}

fun PaymentResponse.toModel(title: String): Payment {
    val valueDecimal = value.filterNot { it.isWhitespace() }.toBigDecimalOrNull()
    val isNegativeValue = valueDecimal?.let { it < 0.toBigDecimal() } ?: false
    val formattedValue = valueDecimal?.formatRoubles() ?: value

    return Payment(
        id = "",
        title = title,
        description = "Пополнение баланса",
        date = date.toDate(),
        value = formattedValue,
        isNegative = isNegativeValue,
    )
}

fun PaygraphResponse.toModel(
    title: String,
    today: LocalDate,
): Payment? {
    val date = LocalDate.parse(datePlan)
    if (date > today) return null
    val sumWithSign = "-$sum"

    val valueDecimal = sumWithSign.toBigDecimalOrNull()
    val isNegativeValue = true
    val formattedValue = valueDecimal?.formatRoubles() ?: sumWithSign

    return Payment(
        id = "",
        title = title,
        description = "Оплата за ${title.decapitalized()}",
        date = date,
        value = formattedValue,
        isNegative = isNegativeValue,
    )
}

fun String.toDate(): LocalDate {
    return JavaLocalDate.parse(this, apiDateFormatter).toKotlinLocalDate()
}

private val apiDateFormatter = DateTimeFormatter.ofPattern("MMMM d',' yyyy", Locale.US)
