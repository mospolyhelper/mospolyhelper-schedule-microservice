package com.mospolytech.data.performance

import com.mospolytech.domain.perfomance.model.Performance
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Serializable
data class PerformanceResponseDto(
    val academicPerformance: List<PerformanceResponse>
)

@Serializable
data class PerformanceResponse(
    val id : Int,
    @SerialName("bill_num")
    val billNum : String,
    @SerialName("bill_type")
    val billType : String,
    @SerialName("doc_type")
    val docType : String,
    val name : String,
    @SerialName("exam_date")
    val examDate : String,
    @SerialName("exam_time")
    val examTime : String,
    val grade : String,
    @SerialName("ticket_num")
    val ticketNum : String,
    val teacher : String,
    val course : Int,
    @SerialName("exam_type")
    val examType : String,
    val chair : String
)

fun PerformanceResponse.toModel(): Performance {
    return Performance(
        id = id,
        billNum = billNum,
        billType = billType,
        docType = docType,
        name = name,
        date = examDate.toDate(),
        time = examTime.toTime(),
        grade = grade,
        ticketNum = ticketNum.ifEmpty { null },
        teacher = teacher,
        course = course,
        examType = examType,
        chair = chair
    )
}

fun String.toDate(): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern("MMMM d',' yyyy", Locale.US))
    } catch (e: Throwable) {
        null
    }
}
fun String.toTime(): LocalTime? {
    return try {
        LocalTime.parse(this)
    } catch (e: Throwable) {
        null
    }
}
