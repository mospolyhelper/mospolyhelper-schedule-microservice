package com.mospolytech.domain.base.utils.converters

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializer(forClass = LocalDate::class)
object LocalDateConverter: KSerializer<LocalDate> {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.encode())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val string = decoder.decodeString()
        return LocalDate.from(dateFormatter.parse(string))
    }

    fun LocalDate.encode() = dateFormatter.format(this)
    fun String.decode() = dateFormatter.parse(this)
}