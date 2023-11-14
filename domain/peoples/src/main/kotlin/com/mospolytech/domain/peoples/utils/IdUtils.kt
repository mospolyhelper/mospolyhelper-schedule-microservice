package com.mospolytech.domain.peoples.utils

import io.ktor.util.*
import java.math.BigInteger
import java.security.MessageDigest

fun String.toGroupId(): String {
    return encodeBase64()
}

fun String.toLessonSubjectId(): String {
    return md5(this)
}

private fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}
