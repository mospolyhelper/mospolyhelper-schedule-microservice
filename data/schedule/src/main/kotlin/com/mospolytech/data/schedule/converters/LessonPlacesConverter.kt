package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.ApiLesson
import com.mospolytech.domain.schedule.model.place.Place
import io.ktor.util.*
import kotlin.text.isLowerCase

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
    val (rawTitle2, type) = parseEmoji(rawTitle)
    val title = processTitle(rawTitle2)

    val url2 = ""

    return Place(
        title.encodeBase64(),
        title,
        getPlaceDescription(title)
//        type,
//        if (url.isEmpty()) url2 else url
    )
}

private fun processTitle(raw: String): String {
    return raw.trim('_')
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

fun getPlaceDescription(title: String): String {
    for (pair in audMap) {
        val regex = Regex(pair.key, RegexOption.IGNORE_CASE)
        if (regex.containsMatchIn(title)) {
            return regex.replace(title, pair.value)
        }
    }
    return title
}

private val audMap = mapOf(
    """^ав\s*((\d)(\d)(.+))$""" to """Автозаводская, к. $2, этаж $3, ауд. $1""",

    """^пр\s*((\d)(\d).+)$""" to """Прянишникова, к. $2, этаж $3, ауд. $1""",
    """^пр\s*ВЦ\s*\d+\s*\(((\d)(\d).+)\)$""" to """Прянишникова, к. $2, этаж $3, ауд. $1""",
    """^пр\s(ФО[\s-]*\d+)$""" to """Прянишникова, к. 2, этаж 4, ауд. $1""",

    """^м\s*((\d)(\d).+)$""" to """Михалковская, к. $2, этаж $3, ауд. $1""",

    """^(\d)пк\s*((\d).+)$""" to """Павла Корчагина, к. $1, этаж $3, ауд. $2""",
    """^пк\s*((\d).+)$""" to """Павла Корчагина, к. 1, этаж $2, ауд. $1""",

    """^([АБВНH]|Нд)\s*(\d).+$""" to """Б. Семёновская, к. $1, этаж $2, ауд. $0""",
    """^(А)[\s-]?ОМД$""" to """Б. Семёновская, к. $1, Лаборатория обработки материалов давлением""",

    """^[_]*ПД[_]*$""" to """Проектная деятельность""",

    """^[_-]*(LMS|ЛМС)[_-]*$""" to """Обучение в ЛМС""",
    """^Обучение\s+в\s+LMS$""" to """Обучение в ЛМС""",
    """^Webex$""" to """Видеоконференция в Webex""",
    """^Webinar$""" to """Онлайн лекция в Webinar""""",

    """^м[\s\p{P}]*спорт[\s\p{P}]*зал[\p{P}]*$""" to """Михалковская, Спортзал""",
    """^Зал\s+№*(\d)[_]*$""" to """Спортивный зал №$1""",
    """^Автозаводская\s+(\d)$""" to """Спортивный зал №$1 (Автозаводская)""",
    """^(.*Измайлово.*)$""" to """$1""",

    """^ИМАШ(\sРАН)?[\s_]*$""" to """Институт машиноведения имени А. А. Благонравова РАН""",
    """^ИОНХ(\sРАН)?[\s_]*$""" to """Институт общей и неорганической химии им. Н.С. Курнакова РАН""",
    """^(.*Биоинженерии.*(РАН)?)$""" to """$1""",
    """^(.*Техноград.*)$""" to """Техноград на ВДНХ""",
    """^МИСиС$""" to """НИТУ МИСиС""",

    """^Практика$""" to """Практика""",
    """^Бизнес.кар$""" to """Группа компаний «БИЗНЕС КАР»""",
)