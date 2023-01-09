package com.mospolytech.data.peoples.service

import com.mospolytech.data.peoples.model.xml.EmployeeInfo
import com.mospolytech.domain.base.AppConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File
import kotlin.io.path.createTempFile

class TeachersService(
    private val client: HttpClient,
    private val appConfig: AppConfig,
) {
    companion object {
        private const val FOLDER_NAME = "teachers"
    }

    @OptIn(ExperimentalXmlUtilApi::class)
    suspend fun parseTeachers(file: File): Sequence<EmployeeInfo> = withContext(Dispatchers.IO) {
        val xml = XML {
            unknownChildHandler = UnknownChildHandler { _, _, _, _, _ -> emptyList() }
        }

        val inputString = file.readText()
            .replaceBefore("<m:ДанныеОРаботнике>", "")
            .replaceAfterLast("</m:ДанныеОРаботнике>", "")
            .replace("\t\t\t\t", "")
            // .replace("""\t<m:Паспорт>[^*]*?</m:ДанныеОРаботнике>""".toRegex(), "</m:ДанныеОРаботнике>")
            .replace("m:", "")

        val teachers = "<ДанныеОРаботнике>[^*]*?</ДанныеОРаботнике>".toRegex()
            .findAll(inputString)
            .map { xml.decodeFromString<EmployeeInfo>(it.value) }
            .filter { it.status == "Работа" }

        return@withContext teachers
    }

    suspend fun downloadTeachers(): File {
        val file = createTempFile(prefix = "staff").toFile()

        client.preparePost(appConfig.getStaffUrl) {
            headers {
                append("Authorization", appConfig.getStaffAuth)
                append(
                    "SOAPAction",
                    "BS%23BS_%D0%92%D1%8B%D0%B3%D1%80%D1%83%D0%B7%D0%BA%D0%B0%D0%92%D0%9B%D0%9A%3A%D0%98" +
                        "%D0%BD%D1%84%D0%BE%D1%80%D0%BC%D0%B0%D1%86%D0%B8%D1%8F%D0%9F%D0%BE%D0%A0%D0%B0%D0%B1" +
                        "%D0%BE%D1%82%D0%BD%D0%B8%D0%BA%D0%B0%D0%BC%D0%98%D0%A4%D0%B8%D0%B7%D0%9B%D0%B8%D1%86" +
                        "%D0%B0%D0%BC%D0%92%D0%A7%D0%B0%D1%81%D1%82%D0%B8%D0%9F%D0%B5%D1%80%D1%81%D0%BE%D0%BD%D0" +
                        "%B0%D0%BB%D1%8C%D0%BD%D1%8B%D1%85%D0%94%D0%B0%D0%BD%D0%BD%D1%8B%D1%85",
                )
            }
            contentType(ContentType.Text.Xml)
            setBody(GET_TEACHERS_BODY)
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

    private val GET_TEACHERS_BODY = """<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ИнформацияПоРаботникамИФизЛицамВЧастиПерсональныхДанных xmlns="BS">
      <GUID></GUID>
      <ПолнаяСинхронизация>true</ПолнаяСинхронизация>
    </ИнформацияПоРаботникамИФизЛицамВЧастиПерсональныхДанных>
  </soap:Body>
</soap:Envelope>"""
}
