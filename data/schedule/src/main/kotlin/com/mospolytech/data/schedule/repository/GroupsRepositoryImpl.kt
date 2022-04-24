package com.mospolytech.data.schedule.repository

import com.mospolytech.domain.schedule.model.group.GroupInfo
import com.mospolytech.domain.schedule.repository.GroupsRepository

class GroupsRepositoryImpl : GroupsRepository {
    private val map = mutableMapOf<String, GroupInfo>()

    override fun add(
        title: String,
        description: String,
        course: String,
        isEvening: Boolean
    ): GroupInfo {
        return GroupInfo(
            id = title,
            title = title,
            description = description,
            course = course,
            isEvening = isEvening
        ).run {
            map.getOrPut(id) { this }
        }
    }

    override fun get(id: String): GroupInfo? {
        return map[id]
    }
}