package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.domain.schedule.model.group.GroupInfo
import com.mospolytech.domain.schedule.repository.GroupsRepository

class LessonGroupsConverter(
    private val groupsRepository: GroupsRepository
) {
    fun convertGroups(groups: List<ApiGroup>): List<GroupInfo> {
        return groups.map {
            groupsRepository.add(
                title = it.title,
                description = "Описание группы",
                course = it.course.toString(),
                isEvening = it.evening != 0
            )
        }
    }
}