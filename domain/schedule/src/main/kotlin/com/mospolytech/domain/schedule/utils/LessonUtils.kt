package com.mospolytech.domain.schedule.utils

import com.mospolytech.domain.schedule.model.pack.CompactLessonEvent

// fun List<LessonDateTimes>.filterByGroup(groupId: String): List<LessonDateTimes> {
//    return this.filter { it.lesson.groups.any { it.title == groupId } }
// }
//
// fun List<LessonDateTimes>.filterByPlace(placeId: String): List<LessonDateTimes> {
//    return this.filter { it.lesson.places.any { it.id == placeId } }
// }
//
// fun List<LessonDateTimes>.filterByTeacher(teacherId: String): List<LessonDateTimes> {
//    return this.filter { it.lesson.teachers.any { it.id == teacherId } }
// }
//
// fun List<LessonDateTimes>.filterByPlaces(placeIds: List<String>): List<LessonDateTimes> {
//    return this.filter { it.lesson.places.any { it.id in placeIds } }
// }
//
// fun List<LessonDateTimes>.filterBySubject(subjectId: String): List<LessonDateTimes> {
//    return this.filter { it.lesson.subject.id == subjectId }
// }
//
// fun List<LessonDateTimes>.filter(filter: ScheduleComplexFilter): List<LessonDateTimes> {
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
// }

fun List<CompactLessonEvent>.filterByPlace(placeId: String): List<CompactLessonEvent> {
    return this.filter { it.placesId.any { it == placeId } }
}

fun List<CompactLessonEvent>.filterByPlaces(placeIds: List<String>): List<CompactLessonEvent> {
    return this.filter { it.placesId.any { it in placeIds } }
}

fun List<CompactLessonEvent>.filterBySubject(subjectId: String): List<CompactLessonEvent> {
    return this.filter { it.subjectId == subjectId }
}

// fun List<CompactLessonEvent>.filter(filter: ScheduleComplexFilter): List<CompactLessonEvent> {
//    val notNeedFilterBySubject = filter.subjectsId.isEmpty()
//    val notNeedFilterByType = filter.typesId.isEmpty()
//    val notNeedFilterByTeachers = filter.teachersId.isEmpty()
//    val notNeedFilterByGroups = filter.groupsId.isEmpty()
//    val notNeedFilterByPlaces = filter.placesId.isEmpty()
//
//    return this.filter {
//        (notNeedFilterBySubject || it.subjectId in filter.subjectsId) &&
//            (notNeedFilterByType || it.type in filter.typesId) &&
//            (notNeedFilterByPlaces || it.placesId.any { it in filter.placesId })
//    }
// }
