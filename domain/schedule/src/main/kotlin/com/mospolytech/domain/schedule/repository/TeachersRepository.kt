package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.teacher.TeacherInfo

interface TeachersRepository {
    fun add(name: String, description: String): TeacherInfo
    fun get(id: String): TeacherInfo?
}