package com.mospolytech.mph.domain.schedule.model

import kotlinx.serialization.Serializable

@Serializable
enum class ScheduleSources {
    Group,
    Teacher,
    Student,
    Place,
    Subject,
    Complex
}