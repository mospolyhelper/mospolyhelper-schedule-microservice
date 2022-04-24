package com.mospolytech.domain.schedule.repository

import com.mospolytech.domain.schedule.model.group.GroupInfo

interface GroupsRepository {
    fun add(
        title: String,
        description: String,
        course: String,
        isEvening: Boolean
    ): GroupInfo
    fun get(id: String): GroupInfo?
}