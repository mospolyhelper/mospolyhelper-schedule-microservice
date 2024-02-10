package com.mospolytech.data.schedule.converters.places

internal const val DUMP_CHARS = """\s\p{P}~`"""
private const val DUMP_CLEANER_REGEX = """[$DUMP_CHARS]*(.*?)[$DUMP_CHARS]*"""
private val regex = DUMP_CLEANER_REGEX.toRegex()

internal fun String.cleanDump(): String {
    return regex.matchEntire(this)?.groupValues?.getOrNull(1) ?: this
}
