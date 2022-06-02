package com.mospolytech.data.peoples

import com.mospolytech.domain.base.model.PagingDTO
import com.mospolytech.domain.peoples.model.Student
import com.mospolytech.domain.peoples.model.Teacher
import com.mospolytech.domain.peoples.repository.PeoplesRepository

class PeoplesRepositoryImpl(
    private val studentsService: StudentsService,
    private val teachersService: TeachersService
): PeoplesRepository {

    private val teachersLocalCache = teachersService.getTeachers()
        .asSequence()
        .map { it.toModel() }
        .distinctBy { it.id }
        .toList()

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

    override suspend fun getStudents(name: String, page: Int) = kotlin.runCatching {
        PagingDTO(1,1,1,emptyList<Student>())
    }

    override suspend fun getStudents(): Result<List<String>> = kotlin.runCatching { studentsService.getStudents() }

    override suspend fun getClassmates(token: String): Result<List<Student>> {
        return kotlin.runCatching { emptyList() }
    }
}