package com.mospolytech.domain.schedule.utils

import com.mospolytech.domain.schedule.model.ScheduleComplexFilter
import com.mospolytech.domain.schedule.model.pack.CompactLessonAndTimes

//fun List<LessonDateTimes>.filterByGroup(groupId: String): List<LessonDateTimes> {
//    return this.filter { it.lesson.groups.any { it.title == groupId } }
//}
//
//fun List<LessonDateTimes>.filterByPlace(placeId: String): List<LessonDateTimes> {
//    return this.filter { it.lesson.places.any { it.id == placeId } }
//}
//
//fun List<LessonDateTimes>.filterByTeacher(teacherId: String): List<LessonDateTimes> {
//    return this.filter { it.lesson.teachers.any { it.id == teacherId } }
//}
//
//fun List<LessonDateTimes>.filterByPlaces(placeIds: List<String>): List<LessonDateTimes> {
//    return this.filter { it.lesson.places.any { it.id in placeIds } }
//}
//
//fun List<LessonDateTimes>.filterBySubject(subjectId: String): List<LessonDateTimes> {
//    return this.filter { it.lesson.subject.id == subjectId }
//}
//
//fun List<LessonDateTimes>.filter(filter: ScheduleComplexFilter): List<LessonDateTimes> {
//    val notNeedFilterBySubject = filter.subjectsId.isEmpty()
//    val notNeedFilterByType = filter.typesId.isEmpty()
//    val notNeedFilterByTeachers = filter.teachersId.isEmpty()
//    val notNeedFilterByGroups = filter.groupsId.isEmpty()
//    val notNeedFilterByPlaces = filter.placesId.isEmpty()
//
//    return this.filter {
//        (notNeedFilterBySubject || it.lesson.subject.id in filter.subjectsId) &&
//                (notNeedFilterByType || it.lesson.type.id in filter.typesId) &&
//                (notNeedFilterByTeachers || it.lesson.teachers.any { it.id in filter.teachersId }) &&
//                (notNeedFilterByGroups || it.lesson.groups.any { it.id in filter.groupsId }) &&
//                (notNeedFilterByPlaces || it.lesson.places.any { it.id in filter.placesId })
//    }
//}





fun List<CompactLessonAndTimes>.filterByGroup(groupId: String): List<CompactLessonAndTimes> {
    return this.filter { it.lesson.groupsId.any { it == groupId } }
}

fun List<CompactLessonAndTimes>.filterByPlace(placeId: String): List<CompactLessonAndTimes> {
    return this.filter { it.lesson.placesId.any { it == placeId } }
}

fun List<CompactLessonAndTimes>.filterByTeacher(teacherId: String): List<CompactLessonAndTimes> {
    return this.filter { it.lesson.teachersId.any { it == teacherId } }
}

fun List<CompactLessonAndTimes>.filterByPlaces(placeIds: List<String>): List<CompactLessonAndTimes> {
    return this.filter { it.lesson.placesId.any { it in placeIds } }
}

fun List<CompactLessonAndTimes>.filterBySubject(subjectId: String): List<CompactLessonAndTimes> {
    return this.filter { it.lesson.subjectId == subjectId }
}

fun List<CompactLessonAndTimes>.filter(filter: ScheduleComplexFilter): List<CompactLessonAndTimes> {
    val notNeedFilterBySubject = filter.subjectsId.isEmpty()
    val notNeedFilterByType = filter.typesId.isEmpty()
    val notNeedFilterByTeachers = filter.teachersId.isEmpty()
    val notNeedFilterByGroups = filter.groupsId.isEmpty()
    val notNeedFilterByPlaces = filter.placesId.isEmpty()

    return this.filter {
        (notNeedFilterBySubject || it.lesson.subjectId in filter.subjectsId) &&
                (notNeedFilterByType || it.lesson.typeId in filter.typesId) &&
                (notNeedFilterByTeachers || it.lesson.teachersId.any { it in filter.teachersId }) &&
                (notNeedFilterByGroups || it.lesson.groupsId.any { it in filter.groupsId }) &&
                (notNeedFilterByPlaces || it.lesson.placesId.any { it in filter.placesId })
    }
}
