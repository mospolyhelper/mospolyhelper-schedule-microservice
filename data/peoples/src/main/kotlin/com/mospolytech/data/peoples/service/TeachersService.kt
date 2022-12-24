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
                    "BS#BS_ВыгрузкаВЛК:ИнформацияПоРаботникамИФизЛицамВЧастиПерсональныхДанных".encodeURLParameter(),
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
