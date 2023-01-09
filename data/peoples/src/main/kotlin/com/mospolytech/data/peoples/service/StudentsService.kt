package com.mospolytech.data.peoples.service

import com.mospolytech.data.peoples.model.xml.StudentXml
import com.mospolytech.domain.base.AppConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File
import java.time.LocalDate

class StudentsService(
    private val client: HttpClient,
    private val appConfig: AppConfig,
) {

    suspend fun parseStudents(file: File): Sequence<StudentXml> = withContext(Dispatchers.IO) {
        val xml = XML {
            unknownChildHandler = UnknownChildHandler { _, _, _, _, _ -> emptyList() }
        }

        val inputString = file.readText()
            .replaceBefore("<m:Состав>", "")
            .replaceAfterLast("</m:Состав>", "")
            .replace("\t\t\t\t", "")
            .replace("m:", "")

        val students = "<Состав>[^*]*?</Состав>".toRegex()
            .findAll(inputString)
            .map { xml.decodeFromString<StudentXml>(it.value) }
        // .filter { it.studentStatus.title == "Является студентом" }

        return@withContext students
    }

    suspend fun downloadStudents(): File {
        val file = kotlin.io.path.createTempFile(prefix = "students").toFile()

        client.preparePost(appConfig.getStudentsUrl) {
            headers {
                append("Authorization", appConfig.getStudentsAuth)
                append("SOAPAction", "http://sgu-infocom.ru/WebExchangeLK#WebExchangeLK:GetUsers")
            }
            contentType(ContentType.Text.Xml)
            setBody(GET_STUDENTS_BODY)
        }.execute { httpResponse ->
            val channel: ByteReadChannel = httpResponse.body()
            while (!channel.isClosedForRead) {
                val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                while (!packet.isEmpty) {
                    val bytes = packet.readBytes()
                    file.appendBytes(bytes)
                }
            }
        }

        return file
    }

    private val GET_STUDENTS_BODY = """<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <GetUsers xmlns="http://sgu-infocom.ru/WebExchangeLK">
      <PersonalGUID></PersonalGUID>
      <ActualStatusDate>${LocalDate.now()}Z</ActualStatusDate>
      <PeriodOfDismiss></PeriodOfDismiss>
      <PeriodOfGraduate></PeriodOfGraduate>
    </GetUsers>
  </soap:Body>
</soap:Envelope>"""
}
