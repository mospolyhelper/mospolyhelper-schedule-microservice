package com.mospolytech.data.performance

import com.mospolytech.domain.perfomance.model.Marks
import com.mospolytech.domain.perfomance.repository.PerformanceRepository

class PerformanceRepositoryImpl: PerformanceRepository {

    override fun getCourses(): List<Int> {
        return getMarks().map { it.course }.toHashSet().toList()
    }

    override fun getSemesters(): List<Int> {
        return getMarks().map { it.semester }
    }

    override fun getMarksByCourse(course: Int): List<Marks> {
        return getMarks().filter { it.course == course }
    }

    override fun getMarksBySemester(semester: Int): Marks {
        return getMarks().first { it.semester == semester }
    }

    override fun getMarks(): List<Marks> {
        return getMarksList()
    }
}