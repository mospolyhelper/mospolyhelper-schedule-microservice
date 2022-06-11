package com.mospolytech.domain.peoples.model

@kotlinx.serialization.Serializable
data class Group(
    val id: String,
    val title: String,
    val course: Int?,
    val faculty: StudentFaculty,
    val direction: StudentDirection,
)