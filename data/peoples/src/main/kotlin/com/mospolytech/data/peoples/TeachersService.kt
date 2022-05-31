package com.mospolytech.data.peoples

import com.mospolytech.domain.base.model.PagingDTO
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File
import java.io.InputStream

object TeachersService {

    private val teachers = getTeachers()

    private fun getTeachers(): List<ДанныеОРаботнике> {
        val xml = XML()
        val inputStream: InputStream = File("data/peoples/src/main/resources/raw/peoples.xml").inputStream()
        val inputString = inputStream
            .bufferedReader()
            .let {
                val string = it.readText()
                it.close()
                string
            }
            .replaceBefore("<m:ДанныеОРаботнике>", "")
            .replaceAfterLast("</m:ДанныеОРаботнике>", "")
            .replace("\t\t\t\t", "")
            .replace("\t<m:Паспорт>[^*]*?</m:ДанныеОРаботнике>".toRegex(), "</m:ДанныеОРаботнике>")
            .replace("m:", "")
        val teachers = "<ДанныеОРаботнике>[^*]*?</ДанныеОРаботнике>".toRegex()
            .findAll(inputString)
            .map { xml.decodeFromString<ДанныеОРаботнике>(it.value) }
            .filter { it.Состояние == "Работа" }
            .toList()
        return teachers
    }

    fun getTeachers(name: String, pageSize: Int = 30, page: Int = 1): PagingDTO<ДанныеОРаботнике> {
        val pages = teachers.filter { it.СотрудникНаименование.contains(name, ignoreCase = true) }.windowed(pageSize, pageSize)
        return when {
            page < 1 -> PagingDTO(pageSize, null, if (pages.size > 1) 2 else null, pages[0])
            page > pages.size -> PagingDTO(pageSize, if (pages.size>1) pages.size - 1 else null, null, pages.lastOrNull() ?: emptyList())
            else -> PagingDTO(pageSize, if (page > 1) page - 1 else null, if (pages.size > page) page + 1 else null, pages[page - 1])
        }
    }

}