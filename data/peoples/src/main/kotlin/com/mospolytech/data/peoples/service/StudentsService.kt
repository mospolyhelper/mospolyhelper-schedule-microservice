package com.mospolytech.data.peoples.service

import com.mospolytech.data.peoples.model.response.PaginationResponse
import com.mospolytech.data.peoples.model.response.StudentsResponse
import com.mospolytech.data.peoples.model.xml.StudentXml
import com.mospolytech.domain.base.AppConfig
import com.mospolytech.domain.base.utils.Moscow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.io.readByteArray
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File

class StudentsService(
    private val client: HttpClient,
    private val appConfig: AppConfig,
) {
    @OptIn(ExperimentalXmlUtilApi::class)
    suspend fun parseStudents(file: File): Sequence<StudentXml> =
        withContext(Dispatchers.IO) {
            val xml =
                XML {
                    defaultPolicy {
                        unknownChildHandler = UnknownChildHandler { _, _, _, _, _ -> emptyList() }
                    }
                }

            val inputString =
                file.readText()
                    .replaceBefore("<m:Состав>", "")
                    .replaceAfterLast("</m:Состав>", "")
                    .replace("\t\t\t\t", "")
                    .replace("m:", "")

            val students =
                "<Состав>[^*]*?</Состав>".toRegex()
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
                while (!packet.exhausted()) {
                    val bytes = packet.readByteArray()
                    file.appendBytes(bytes)
                }
            }
        }

        return file
    }

    suspend fun getStudentsLk(
        token: String,
        search: String? = null,
        group: String? = null,
        page: Int = 1,
        perpage: Int = 100,
    ): PaginationResponse<StudentsResponse> {
        return client.get(GET_STUDENTS) {
            parameter("token", token)
            parameter("search", search)
            parameter("group", group)
            parameter("page", page)
            parameter("perpage", perpage)
        }.body()
    }

    companion object {
        private const val BASE_URL = "https://e.mospolytech.ru"
        private const val API_URL = "$BASE_URL/old/lk_api.php"

        private const val GET_STUDENTS = "$API_URL?getStudents="

        private val GET_STUDENTS_BODY = """<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <GetUsers xmlns="http://sgu-infocom.ru/WebExchangeLK">
      <PersonalGUID></PersonalGUID>
      <ActualStatusDate>${Clock.System.todayIn(TimeZone.Moscow)}Z</ActualStatusDate>
      <PeriodOfDismiss></PeriodOfDismiss>
      <PeriodOfGraduate></PeriodOfGraduate>
    </GetUsers>
  </soap:Body>
</soap:Envelope>"""
    }
}
