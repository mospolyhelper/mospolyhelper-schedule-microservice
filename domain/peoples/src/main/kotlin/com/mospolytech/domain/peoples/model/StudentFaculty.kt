package com.mospolytech.domain.peoples.model

@kotlinx.serialization.Serializable
data class StudentFaculty(
    val id: String,
    val title: String,
    val titleShort: String?
)