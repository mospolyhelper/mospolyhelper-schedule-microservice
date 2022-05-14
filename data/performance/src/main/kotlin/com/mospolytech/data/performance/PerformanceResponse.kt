package com.mospolytech.data.performance

import com.mospolytech.domain.perfomance.model.Performance
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class PerformanceResponseDto(
    val academicPerformance: List<PerformanceResponse>
)

@Serializable
data class PerformanceResponse(
    val id : Int,
    @SerialName("bill_num")
    val billNum : Int,
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
        dateTime = dateTimeToLocal(examDate, examTime),
        grade = grade,
        ticketNum = ticketNum.ifEmpty { null },
        teacher = teacher,
        course = course,
        examType = examType,
        chair = chair
    )
}

fun dateTimeToLocal(date: String, time: String): LocalDateTime {
    return LocalDateTime.MAX
}
