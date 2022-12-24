package com.mospolytech.domain.perfomance.model

import kotlinx.serialization.Serializable

@Serializable
data class SemestersWithCourse(
    val coursesWithSemesters: Map<Int, Int>,
)
