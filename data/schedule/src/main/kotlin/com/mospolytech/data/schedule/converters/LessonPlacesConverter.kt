package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.ApiLesson
import com.mospolytech.domain.schedule.model.Place

object LessonPlacesConverter {


    fun convertPlaces(auditoriums: List<ApiLesson.Auditory>, url: String = ""): List<Place> {

        return auditoriums.map { processAuditorium(it.title, url) }
    }
}

//val Place.isOnline: Boolean
//    get() =
//        url.isNotEmpty() ||
//                (AuditoriumTypes.values()
//                    .firstOrNull { it.type == type }?.isOnline
//                    ?: false)

enum class AuditoriumTypes(val type: String, val isOnline: Boolean) {
    Webinar("Вебинар", true),
    Lms("LMS", true),
    VideoConference("Видеоконф.", true),
    Other("", false)
}
private val regex = Regex("""href="(.*?)".*?>(.*?)<""")


fun processAuditorium(auditorium: String, url: String): Place {
    val regGroups = regex.find(auditorium)?.groupValues
    val (url, rawTitle0) = if (regGroups != null)
        regGroups.getOrNull(1) to regGroups.getOrNull(2)
    else
        null to null

    val parsedHtml = rawTitle0 ?: auditorium
    val rawTitle = parsedHtml.trim()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    val (title, type) = parseEmoji(rawTitle)
    val url2 = ""

    return Place(
        title,
//        type,
//        if (url.isEmpty()) url2 else url
    )
}

private val emojis = listOf(
    "\uD83D\uDCF7" to "Вебинар",    // 📷
    "\uD83C\uDFE0" to "LMS",    // 🏠
    "\uD83D\uDCBB" to "Видеоконф."  // 💻
)

fun parseEmoji(raw: String): Pair<String, String> {
    val emoji = emojis.firstOrNull { raw.contains(it.first) }
    return if (emoji == null)
        Pair(raw.trim(), "")
    else
        Pair(raw.replace(emoji.first, "").trim(), emoji.second)
}