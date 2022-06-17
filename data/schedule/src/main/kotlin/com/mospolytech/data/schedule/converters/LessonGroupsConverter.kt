package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.response.ApiGroup
import com.mospolytech.domain.schedule.repository.GroupsRepository

class LessonGroupsConverter(
    private val groupsRepository: GroupsRepository
) {
    suspend fun convertGroups(groups: List<ApiGroup>): List<String> {
        return groups.map {
            groupsRepository.getOrPut(
                title = it.title,
                course = it.course.toString(),
                isEvening = it.evening != 0
            )
        }
    }
}