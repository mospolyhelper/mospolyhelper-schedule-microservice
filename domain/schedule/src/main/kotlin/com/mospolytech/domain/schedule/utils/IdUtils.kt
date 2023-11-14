package com.mospolytech.domain.schedule.utils

import io.ktor.util.*

fun String.toLessonTypeId(): String {
    return encodeBase64()
}
