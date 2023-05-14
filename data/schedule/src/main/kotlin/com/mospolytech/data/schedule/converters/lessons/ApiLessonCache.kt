package com.mospolytech.data.schedule.converters.lessons

class ApiLessonCache(
    val typeId: String,
    val subjectId: String,
    val teachersId: List<String>,
    val placesId: List<String>,
    val timesId: List<String>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApiLessonCache

        if (typeId != other.typeId) return false
        if (subjectId != other.subjectId) return false
        if (teachersId != other.teachersId) return false
        if (placesId != other.placesId) return false
        return timesId == other.timesId
    }

    override fun hashCode(): Int {
        var result = typeId.hashCode()
        result = 31 * result + subjectId.hashCode()
        result = 31 * result + teachersId.hashCode()
        result = 31 * result + placesId.hashCode()
        result = 31 * result + timesId.hashCode()
        return result
    }
}
