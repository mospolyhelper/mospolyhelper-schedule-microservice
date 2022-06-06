package com.mospolytech.data.peoples

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.repository.PeoplesRepository
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PeoplesRepositoryImpl(
    studentsService: StudentsService,
    teachersService: TeachersService,
    private val peoplesDb: PeoplesDb
): PeoplesRepository {

    private val teachersLocalCache by lazy {
        teachersService.getTeachers()
            .asSequence()
            .map { it.toModel() }
            .distinctBy { it.id }
            .toList()
    }

    private val studentsLocalCache by lazy {
        studentsService.getStudents()
            .asSequence()
            .map { it.toModel() }
            .distinctBy { it.id }
            .toList()
    }

    override fun getTeachers(name: String, page: Int, pageSize: Int): PagingDTO<Teacher> {
        val pages = teachersLocalCache
            .asSequence()
            .filter { it.name.contains(name, ignoreCase = true) }
            .windowed(pageSize, pageSize, partialWindows = true)
            .toList()

        val fixedPage = (page - 1).let {
            when {
                pages.isEmpty() -> null
                it < 1 -> 0
                it > pages.lastIndex -> null
                else -> it
            }
        }

        val data = if (fixedPage == null) emptyList() else pages[fixedPage]

        return PagingDTO(
            count = data.size,
            previousPage = if (fixedPage == 0) null else fixedPage ?: pages.size,
            nextPage = fixedPage?.let { if (fixedPage == pages.lastIndex) null else it + 2 },
            data = data
        )
    }

    override fun getTeachers(): List<Teacher> {
        return teachersLocalCache
    }

    override fun getStudents(name: String, page: Int, pageSize: Int): PagingDTO<Student> {
        val pages = studentsLocalCache
            .asSequence()
            .filter { it.getFullName().contains(name, ignoreCase = true) }
            .windowed(pageSize, pageSize, partialWindows = true)
            .toList()

        val fixedPage = (page - 1).let {
            when {
                pages.isEmpty() -> null
                it < 1 -> 0
                it > pages.lastIndex -> null
                else -> it
            }
        }

        val data = if (fixedPage == null) emptyList() else pages[fixedPage]

        return PagingDTO(
            count = data.size,
            previousPage = if (fixedPage == 0) null else fixedPage ?: pages.size,
            nextPage = fixedPage?.let { if (fixedPage == pages.lastIndex) null else it + 2 },
            data = data
        )
    }

    override fun getStudents(): List<Student> = studentsLocalCache

    override fun getClassmates(group: String): List<Student> {
        return studentsLocalCache.filter { it.group == group }
    }

    override fun updateData() {
        peoplesDb.clearData()
        teachersLocalCache.forEach(peoplesDb::addTeacher)
        studentsLocalCache.forEach(peoplesDb::addStudent)
    }

}
