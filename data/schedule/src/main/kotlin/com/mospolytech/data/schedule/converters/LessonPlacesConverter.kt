package com.mospolytech.data.schedule.converters

import com.mospolytech.data.schedule.model.ApiLesson
import com.mospolytech.domain.base.model.Location
import com.mospolytech.domain.base.utils.capitalized
import com.mospolytech.domain.schedule.model.place.PlaceInfo
import com.mospolytech.domain.schedule.repository.PlacesRepository

class LessonPlacesConverter(
    private val placesRepository: PlacesRepository
) {
    fun convertPlaces(auditoriums: List<ApiLesson.Auditory>, url: String = ""): List<PlaceInfo> {
        return auditoriums.map { processAuditorium(it.title, url) }
    }

    private val regex = Regex("""href="(.*?)".*?>(.*?)<""")


    private fun processAuditorium(auditorium: String, url: String): PlaceInfo {
        val regGroups = regex.find(auditorium)?.groupValues
        val (url2, rawTitle0) = if (regGroups != null)
            regGroups.getOrNull(1) to regGroups.getOrNull(2)
        else
            null to null

        val parsedHtml = rawTitle0 ?: auditorium
        val rawTitle = parsedHtml.trim()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        val (rawTitle2, type) = parseEmoji(rawTitle)
        val title = processTitle(rawTitle2)

        val finalUrl = url.ifEmpty { url2 ?: "" }

        return parsePlace(title, finalUrl)
    }

    private fun processTitle(raw: String): String {
        return fixTitle(raw.trim('_', '-'))
    }

    private fun fixTitle(raw: String): String {
        return when (raw) {
            "3301а" -> "М3301а"
            "2202а" -> "Пр2202а"
            else -> raw
        }
    }

    private val emojis = listOf(
        "\uD83D\uDCF7" to "Вебинар",    // 📷
        "\uD83C\uDFE0" to "LMS",    // 🏠
        "\uD83D\uDCBB" to "Видеоконф.", // 💻
        "\uD83C\uDF10" to "Online курс", // 🌐
    )

    private fun parseEmoji(raw: String): Pair<String, String> {
        val emoji = emojis.firstOrNull { raw.contains(it.first) }
        return if (emoji == null)
            raw.trim() to ""
        else
            raw.replace(emoji.first, "").trim() to emoji.second
    }

    private fun parsePlace(place: String, url: String = ""): PlaceInfo {
        return parserChain.firstNotNullOfOrNull {
            val matchResult = place.parseBy(patterns = it.patterns.toTypedArray())
            if (matchResult == null)
                null
            else
                it.placeFactory(matchResult, listOf(url))
        } ?: placesRepository.addUnclassified(place)
    }


    data class PlaceParserPack(
        val patterns: List<String>,
        val placeFactory: MatchResult.(List<String>) -> PlaceInfo
    ) {
        constructor(vararg patterns: String, placeFactory: MatchResult.(List<String>) -> PlaceInfo) :
                this(patterns.toList(), placeFactory)
    }

    private val otherMap = mapOf(
        """^ИМАШ(\sРАН)?[\s_\.]*$""" to """Институт машиноведения имени А. А. Благонравова РАН""",
        """^ИОНХ(\sРАН)?[\s_]*$""" to """Институт общей и неорганической химии им. Н.С. Курнакова РАН""",
        """^(.*Биоинженерии.*(РАН)?)$""" to """$1""",
        """^(.*Техноград.*)$""" to """Техноград на ВДНХ""",
        """^МИСиС$""" to """НИТУ МИСиС""",

        """^Практика$""" to """Практика""",
        """^Бизнес.кар$""" to """Группа компаний «БИЗНЕС КАР»""",
    )

    private val parserChain = listOf(
        PlaceParserPack("""^ав\s*((\d)(\d)(.+))$""") {
            placesRepository.addBuilding(
                title = groupValues[0],
                areaAlias = "Автозаводская",
                street = "Автозаводская улица, 16",
                building = groupValues[2],
                floor = groupValues[3],
                auditorium = groupValues[1],
                location = when (groupValues[2]) {
                    "1" -> Location(55.704191, 37.645163)
                    "2" -> Location(55.704561, 37.645704)
                    "3" -> Location(55.704839, 37.646956)
                    "4" -> Location(55.704452, 37.646639)
                    "5" -> Location(55.705504, 37.646804)
                    "6" -> Location(55.704282, 37.646083)
                    else -> null
                }
            )
        },
        PlaceParserPack("""^пр\s*((\d)(\d).+)$""") {
            placesRepository.addBuilding(
                title = groupValues[0],
                areaAlias = "Прянишникова",
                street = "улица Прянишникова, 2А",
                building = groupValues[2],
                floor = groupValues[3],
                auditorium = groupValues[1],
                location = when (groupValues[2]) {
                    "1" -> Location(55.833268, 37.544180)
                    "2" -> Location(55.833708, 37.543758)
                    else -> null
                }
            )
        },
        PlaceParserPack("""^пр\s*ВЦ\s*\d+\s*\(((\d)(\d).+)\)$""") {
            val building = groupValues[2]

            placesRepository.addBuilding(
                title = groupValues[0],
                areaAlias = "Прянишникова",
                street = "улица Прянишникова, 2А",
                building = building,
                floor = groupValues[3],
                auditorium = groupValues[1],
                location = when (building) {
                    "1" -> Location(55.833268, 37.544180)
                    "2" -> Location(55.833708, 37.543758)
                    else -> null
                }
            )
        },
        PlaceParserPack("""^пр\s(ФО[\s-]*\d+)$""") {
            placesRepository.addBuilding(
                title = groupValues[0],
                areaAlias = "Прянишникова",
                street = "улица Прянишникова, 2А",
                building = "2",
                floor = "4",
                auditorium = groupValues[1],
                location = Location(55.833708, 37.543758)
            )
        },
        PlaceParserPack("""^м\s*((\d)(\d).+)$""") {
            val building = groupValues[2]

            placesRepository.addBuilding(
                title = groupValues[0],
                areaAlias = "Михалковская",
                street = "Михалковская улица, 7",
                building = building,
                floor = groupValues[3],
                auditorium = groupValues[1],
                location = when (building) {
                    "3" -> Location(55.837459, 37.533427)
                    else -> null
                }
            )
        },
        PlaceParserPack("""^м\s*(эстамп)$""") {
            placesRepository.addBuilding(
                title = groupValues[0],
                areaAlias = "Михалковская",
                street = "Михалковская улица, 7",
                location = Location(55.837131, 37.533649)
            )
        },
        PlaceParserPack("""^(\d)пк\s*((\d).+)$""") {
            val building = groupValues[1]

            placesRepository.addBuilding(
                title = groupValues[0],
                areaAlias = "Павла Корчагина",
                street = "улица Павла Корчагина, 22",
                building = building,
                floor = groupValues[3],
                auditorium = groupValues[2],
                location = when (building) {
                    "1" -> Location(55.819439, 37.663351)
                    "2" -> Location(55.819287, 37.664276)
                    else -> null
                }
            )
        },
        PlaceParserPack("""^пк\s*((\d).+)$""") {
            placesRepository.addBuilding(
                title = groupValues[0],
                areaAlias = "Павла Корчагина",
                street = "улица Павла Корчагина, 22",
                building = "1",
                floor = groupValues[2],
                auditorium = groupValues[1],
                location = Location(55.819439, 37.663351)
            )
        },
        PlaceParserPack("""^([АБВНH]|Нд)\s*(\d).+$""") {
            val building = groupValues[1].replace('H', 'Н').lowercase().capitalized()

            placesRepository.addBuilding(
                title = groupValues[0],
                areaAlias = "Б. Семёновская",
                street = "Большая Семёновская улица, 38",
                building = building,
                floor = groupValues[2],
                auditorium = groupValues[0],
                location = when (building) {
                    "А" -> Location(55.781310, 37.711513)
                    "Б" -> Location(55.781074, 37.712677)
                    "В" -> Location(55.781428, 37.710494)
                    "Н" -> Location(55.780930, 37.709807)
                    "Нд" -> Location(55.780300, 37.709753)
                    "Л" -> Location(55.781164, 37.710555)
                    else -> null
                }
            )
        },
        PlaceParserPack("""^(А)[\s-]?ОМД$""") {
            val building = groupValues[1].lowercase().capitalized()

            placesRepository.addBuilding(
                title = groupValues[0],
                areaAlias = "Б. Семёновская",
                street = "Большая Семёновская улица, 38",
                building = building,
                floor = groupValues[1],
                auditorium = "А ОМД",
                location = when (building) {
                    "А" -> Location(55.781310, 37.711513)
                    "Б" -> Location(55.781074, 37.712677)
                    "В" -> Location(55.781428, 37.710494)
                    "Н" -> Location(55.780930, 37.709807)
                    "Нд" -> Location(55.780300, 37.709753)
                    "Л" -> Location(55.781164, 37.710555)
                    else -> null
                },
                description = mapOf("" to "Лаборатория обработки материалов давлением")
            )
        },
        PlaceParserPack("""^Зал\s+№*(\d)[_]*$""") {
            val gymNumber = groupValues[1]

            // Спортивный зал №1 ул. Большая Семеновская, 38, пом. 112а
            // Спортивный зал №2 ул. Семеновская, 38, корпус Б, цокольный этаж
            // Спортивный зал №3 ул. Малая Семеновская, 12
            // Спортивный зал №4 ул. Павла Корчагина, 22, ауд.103
            // Спортивный зал №5 ул. Павла Корчагина, 22, ауд.102
            // Спортивный зал №6 ул. Бориса Галушкина, 9, 2 этаж
            // Спортивный зал №7 ул. Павла Корчагина, 22, пом. No519
            // Спортивный зал №9 ул. Автозаводская, 16, стр. 3, этаж 5, ауд. 3501

            val location = when (gymNumber) {
                "1" -> Location(55.837495, 37.532223)
                "2" -> Location(55.781074, 37.712677)
                "3" -> Location(55.819287, 37.664276)
                "4" -> Location(55.819287, 37.664276)
                "5" -> Location(55.819287, 37.664276)
                "6" -> Location(55.819287, 37.664276)
                "7" -> Location(55.819287, 37.664276)
                "9" -> Location(55.819287, 37.664276)
                else -> null
            }

            val street = when (gymNumber) {
                "1" -> "Большая Семёновская улица, 38"
                "2" -> "Большая Семёновская улица, 38"
                "3" -> "Малая Семёновская улица, 12"
                "4" -> "улица Павла Корчагина, 22"
                "5" -> "улица Павла Корчагина, 22"
                "6" -> "улица Бориса Галушкина, 9"
                "7" -> "улица Павла Корчагина, 22"
                "9" -> "Автозаводская улица, 16"
                else -> null
            }

            val description1 = when (gymNumber) {
                "1" -> "Футбол/футзал"
                "2" -> "Тренажерный зал"
                "3" -> "Единоборства, смешанные боевые единоборства (ММА)"
                "4" -> "Тренажерный зал, Пауэрлифтинг, Кроссфит"
                "5" -> "Бокс/кикбоксинг"
                "6" -> "Тренажерный зал, Волейбол"
                "7" -> "Капоэйра"
                "9" -> "Оздоровительная физическая культура"
                else -> null
            }
            val description = if (description1 != null)
                mapOf("Учебные и тренировочные занятия" to description1)
            else
                mapOf()

            placesRepository.addBuilding(
                title = "Спортзал №${gymNumber}",
                areaAlias = "Спортивный зал №${gymNumber}",
                street = street,
                location = location,
                description = description
            )
        },
        PlaceParserPack("""^м[\s\p{P}]*спорт[\s\p{P}]*зал[\p{P}]*$""") {
            placesRepository.addBuilding(
                title = "М Спортзал",
                areaAlias = "Михалковская",
                street = "Михалковская улица, 7с2",
                location = Location(55.837495, 37.532223),
                description = mapOf(
                    "Учебные и тренировочные занятия"
                            to
                            "Зал спортивных игр, тренажерный зал, залы настольного тенниса, " +
                            "спортивных единоборств, фехтования, физической реабилитации"
                )
            )
        },
        PlaceParserPack("""^Автозаводская\s+(\d)$""") {
            val gymNumber = groupValues[1]

            placesRepository.addBuilding(
                title = "Ав Спортзал №${gymNumber}",
                areaAlias = "Спорткомплекс №${gymNumber} «На Автозаводской»",
                street = "Автозаводская улица, 16с2",
                floor = "8",
                location = Location(55.837495, 37.532223),
                description = mapOf(
                    "Учебные и тренировочные занятия"
                            to
                            "Тренажерный зал, армрестлинг, аскетбол, дартс, настольный теннис, эстетическая гимнастика"
                )
            )
        },
        PlaceParserPack("""^АВ[\s\p{P}]*Спортзал$""") {
            placesRepository.addBuilding(
                title = "Ав Спортзал",
                areaAlias = "Спорткомплекс «На Автозаводской»",
                street = "Автозаводская улица, 16с2",
                floor = "8",
                location = Location(55.837495, 37.532223),
                description = mapOf(
                    "Учебные и тренировочные занятия"
                            to
                            "Тренажерный зал, армрестлинг, аскетбол, дартс, настольный теннис, эстетическая гимнастика"
                )
            )
        },
        PlaceParserPack("""^(.*Измайлово.*)$""") {
            placesRepository.addBuilding(
                title = groupValues[1],
                areaAlias = "Спорткомплекс «Измайлово»",
                street = "11-я Парковая улица, 36с2",
                location = Location(55.800985, 37.806210),
                description = mapOf(
                    "Учебные и тренировочные занятия"
                            to
                            "Тренажерный зал, волейбол, дартс, настольный теннис, степ-аэробика, " +
                            "футбол/футзал (уличная площадка), фитнес-аэробика"
                )
            )
        },
        PlaceParserPack("""^[_\s\.]*Ц?ПД[_\s\.\d]*$""", """^Проектная\sдеятельность$""") {
            placesRepository.addOther("Проектная деятельность")
        },
        PlaceParserPack(
            """^[_-]*(LMS|ЛМС)[_-]*$""",
            """^Обучение\s+в\s+(LMS|ЛМС)$""",
            """^Обучение\s+(LMS|ЛМС)$"""
        ) {
            placesRepository.addOnline(
                title = "Обучение в ЛМС",
                url = it.firstOrNull()
            )
        },
        PlaceParserPack("""^Webex$""") {
            placesRepository.addOnline(
                title = "Видеоконференция в Webex",
                url = it.firstOrNull()
            )
        },
        PlaceParserPack("""^Webinar$""") {
            placesRepository.addOnline(
                title = "Онлайн лекция в Webinar",
                url = it.firstOrNull()
            )
        },
        PlaceParserPack("""^Online\sкурс$""") {
            placesRepository.addOnline(
                title = "Онлайн курс",
                url = it.firstOrNull()
            )
        },
        PlaceParserPack("""^Онлайн$""") {
            placesRepository.addOnline(
                title = "Онлайн курс",
                url = it.firstOrNull()
            )
        },
        PlaceParserPack(*otherMap.keys.toTypedArray()) {
            val title = this.value

            val description1 = otherMap.toList().firstNotNullOf { (key, value) ->
                val regex = Regex(key, RegexOption.IGNORE_CASE)
                if (regex.containsMatchIn(title))
                    regex.replace(title, value)
                else
                    null
            }

            placesRepository.addOther(
                title = title,
                description = mapOf("" to description1)
            )
        }
    )

    private fun String.parseBy(vararg patterns: String): MatchResult? {
        return patterns.firstNotNullOfOrNull {
            val regex = Regex(it, RegexOption.IGNORE_CASE)
            regex.matchEntire(this)
        }
    }
}