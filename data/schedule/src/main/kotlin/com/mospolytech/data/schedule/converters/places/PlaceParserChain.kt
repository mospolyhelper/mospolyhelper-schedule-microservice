package com.mospolytech.data.schedule.converters.places

import com.mospolytech.domain.base.model.Coordinates
import com.mospolytech.domain.base.utils.capitalized
import com.mospolytech.domain.base.utils.ifNotEmpty
import com.mospolytech.domain.schedule.model.place.PlaceInfo

internal data class PlaceParserPack(
    val patterns: List<String>,
    val placeFactory: MatchResult.(List<String>) -> PlaceInfo,
) {
    constructor(vararg patterns: String, placeFactory: MatchResult.(List<String>) -> PlaceInfo) :
        this(patterns.toList(), placeFactory)
}

private val otherMap =
    mapOf(
        """^ИМАШ(\sРАН)?[\s_\.]*$""" to """Институт машиноведения имени А. А. Благонравова РАН""",
        """^ИОНХ(\sРАН)?[\s_]*$""" to """Институт общей и неорганической химии им. Н.С. Курнакова РАН""",
        """^(.*Биоинженерии.*(РАН)?)$""" to """$1""",
        """^(.*Техноград.*)$""" to """Техноград на ВДНХ""",
        """^МИСиС$""" to """НИТУ МИСиС""",
        """^Практика$""" to """Практика""",
        """^Бизнес.кар$""" to """Группа компаний «БИЗНЕС КАР»""",
    )

internal val parserChain =
    listOf(
        PlaceParserPack("""^ав\s*((\d)(\d)(.+))$""") {
            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Автозаводская",
                street = "Автозаводская ул., 16",
                building = groupValues[2],
                floor = groupValues[3],
                auditorium = groupValues[1],
                coordinates =
                    when (groupValues[2]) {
                        "1" -> Coordinates(55.704191, 37.645163)
                        "2" -> Coordinates(55.704561, 37.645704)
                        "3" -> Coordinates(55.704839, 37.646956)
                        "4" -> Coordinates(55.704452, 37.646639)
                        "5" -> Coordinates(55.705504, 37.646804)
                        "6" -> Coordinates(55.704282, 37.646083)
                        else -> null
                    },
            )
        },
        PlaceParserPack("""^пр\s*((\d)(\d).+)$""") {
            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Прянишникова",
                street = "ул. Прянишникова, 2А",
                building = groupValues[2],
                floor = groupValues[3],
                auditorium = groupValues[1],
                coordinates =
                    when (groupValues[2]) {
                        "1" -> Coordinates(55.833268, 37.544180)
                        "2" -> Coordinates(55.833708, 37.543758)
                        else -> null
                    },
            )
        },
        PlaceParserPack("""^пр\s*ВЦ\s*\d+\s*\(((\d)(\d).+)\)$""") {
            val building = groupValues[2]

            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Прянишникова",
                street = "ул. Прянишникова, 2А",
                building = building,
                floor = groupValues[3],
                auditorium = groupValues[1],
                coordinates =
                    when (building) {
                        "1" -> Coordinates(55.833268, 37.544180)
                        "2" -> Coordinates(55.833708, 37.543758)
                        else -> null
                    },
            )
        },
        PlaceParserPack("""^пр\s(ФО[\s-]*\d+)$""") {
            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Прянишникова",
                street = "ул. Прянишникова, 2А",
                building = "2",
                floor = "4",
                auditorium = groupValues[1],
                coordinates = Coordinates(55.833708, 37.543758),
            )
        },
        PlaceParserPack("""^м\s*((\d)(\d).+)$""") {
            val building = groupValues[2]

            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Михалковская",
                street = "Михалковская ул., 7",
                building = building,
                floor = groupValues[3],
                auditorium = groupValues[1],
                coordinates =
                    when (building) {
                        "3" -> Coordinates(55.837459, 37.533427)
                        else -> null
                    },
            )
        },
        PlaceParserPack("""^м\s*(эстамп)$""") {
            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Михалковская",
                street = "Михалковская ул., 7",
                coordinates = Coordinates(55.837131, 37.533649),
            )
        },
        PlaceParserPack("""^(\d)пк\s*((\d).+)$""") {
            val building = groupValues[1]

            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Павла Корчагина",
                street = "ул. Павла Корчагина, 22",
                building = building,
                floor = groupValues[3],
                auditorium = groupValues[2],
                coordinates =
                    when (building) {
                        "1" -> Coordinates(55.819439, 37.663351)
                        "2" -> Coordinates(55.819287, 37.664276)
                        else -> null
                    },
            )
        },
        PlaceParserPack("""^пк\s*((\d).+)$""") {
            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Павла Корчагина",
                street = "ул. Павла Корчагина, 22",
                building = "1",
                floor = groupValues[2],
                auditorium = groupValues[1],
                coordinates = Coordinates(55.819439, 37.663351),
            )
        },
        PlaceParserPack("""^([АБВНH]|Нд)\s*(\d).+$""") {
            val building = groupValues[1].replace('H', 'Н').lowercase().capitalized()

            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Б. Семёновская",
                street = "Большая Семёновская ул., 38",
                building = building,
                floor = groupValues[2],
                auditorium = groupValues[0],
                coordinates =
                    when (building) {
                        "А" -> Coordinates(55.781310, 37.711513)
                        "Б" -> Coordinates(55.781074, 37.712677)
                        "В" -> Coordinates(55.781428, 37.710494)
                        "Н" -> Coordinates(55.780930, 37.709807)
                        "Нд" -> Coordinates(55.780300, 37.709753)
                        "Л" -> Coordinates(55.781164, 37.710555)
                        else -> null
                    },
            )
        },
        PlaceParserPack("""^(А)[\s-]?ОМД$""") {
            val building = groupValues[1].lowercase().capitalized()

            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Б. Семёновская",
                street = "Большая Семёновская ул., 38",
                building = building,
                floor = groupValues[1],
                auditorium = "А ОМД",
                coordinates =
                    when (building) {
                        "А" -> Coordinates(55.781310, 37.711513)
                        "Б" -> Coordinates(55.781074, 37.712677)
                        "В" -> Coordinates(55.781428, 37.710494)
                        "Н" -> Coordinates(55.780930, 37.709807)
                        "Нд" -> Coordinates(55.780300, 37.709753)
                        "Л" -> Coordinates(55.781164, 37.710555)
                        else -> null
                    },
                additionalDescription = "Лаборатория обработки материалов давлением",
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

            val coordinates =
                when (gymNumber) {
                    "1" -> Coordinates(55.837495, 37.532223)
                    "2" -> Coordinates(55.781074, 37.712677)
                    "3" -> Coordinates(55.819287, 37.664276)
                    "4" -> Coordinates(55.819287, 37.664276)
                    "5" -> Coordinates(55.819287, 37.664276)
                    "6" -> Coordinates(55.819287, 37.664276)
                    "7" -> Coordinates(55.819287, 37.664276)
                    "9" -> Coordinates(55.819287, 37.664276)
                    else -> null
                }

            val street =
                when (gymNumber) {
                    "1" -> "Большая Семёновская ул., 38"
                    "2" -> "Большая Семёновская ул., 38"
                    "3" -> "Малая Семёновская ул., 12"
                    "4" -> "ул. Павла Корчагина, 22"
                    "5" -> "ул. Павла Корчагина, 22"
                    "6" -> "ул. Бориса Галушкина, 9"
                    "7" -> "ул. Павла Корчагина, 22"
                    "9" -> "Автозаводская ул., 16"
                    else -> null
                }

            val description1 =
                when (gymNumber) {
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
            val description = description1?.let { "Учебные и тренировочные занятия: $description1" }

            createBuildingPlace(
                title = "Спортзал №$gymNumber",
                areaAlias = "Спортивный зал №$gymNumber",
                street = street,
                coordinates = coordinates,
                additionalDescription = description,
            )
        },
        PlaceParserPack("""^м[\s\p{P}]*спорт[\s\p{P}]*зал[\p{P}]*$""") {
            createBuildingPlace(
                title = "М Спортзал",
                areaAlias = "Михалковская",
                street = "Михалковская ул., 7с2",
                coordinates = Coordinates(55.837495, 37.532223),
                additionalDescription =
                    "Учебные и тренировочные занятия: " +
                        "Зал спортивных игр, тренажерный зал, залы настольного тенниса, " +
                        "спортивных единоборств, фехтования, физической реабилитации",
            )
        },
        PlaceParserPack("""^Автозаводская\s+(\d)$""") {
            val gymNumber = groupValues[1]

            createBuildingPlace(
                title = "Ав Спортзал №$gymNumber",
                areaAlias = "Спорткомплекс №$gymNumber «На Автозаводской»",
                street = "Автозаводская ул., 16с2",
                floor = "8",
                coordinates = Coordinates(55.837495, 37.532223),
                additionalDescription =
                    "Учебные и тренировочные занятия: " +
                        "Тренажерный зал, армрестлинг, аскетбол, дартс, настольный теннис, эстетическая гимнастика",
            )
        },
        PlaceParserPack("""^АВ[\s\p{P}]*Спортзал$""") {
            createBuildingPlace(
                title = "Ав Спортзал",
                areaAlias = "Спорткомплекс «На Автозаводской»",
                street = "Автозаводская ул., 16с2",
                floor = "8",
                coordinates = Coordinates(55.837495, 37.532223),
                additionalDescription =
                    "Учебные и тренировочные занятия: " +
                        "Тренажерный зал, армрестлинг, аскетбол, дартс, настольный теннис, эстетическая гимнастика",
            )
        },
        PlaceParserPack("""^(.*Измайлово.*)$""") {
            createBuildingPlace(
                title = groupValues[1],
                areaAlias = "Спорткомплекс «Измайлово»",
                street = "11-я Парковая ул., 36с2",
                coordinates = Coordinates(55.800985, 37.806210),
                additionalDescription =
                    "Учебные и тренировочные занятия: " +
                        "Тренажерный зал, волейбол, дартс, настольный теннис, степ-аэробика, " +
                        "футбол/футзал (уличная площадка), фитнес-аэробика",
            )
        },
        PlaceParserPack("""^[_\s\.]*Ц?ПД[_\s\.\d]*$""", """^Проектная\sдеятельность$""") {
            createOtherPlace(
                title = "Проектная деятельность",
            )
        },
        PlaceParserPack(
            """^[_-]*(LMS|ЛМС)[_-]*$""",
            """^Обучение\s+в\s+(LMS|ЛМС)$""",
            """^Обучение\s+(LMS|ЛМС)$""",
        ) {
            createOnlinePlace(
                title = "Обучение в ЛМС",
                url = it.firstOrNull(),
            )
        },
        PlaceParserPack("""^Webex$""") {
            createOnlinePlace(
                title = "Видеоконференция в Webex",
                url = it.firstOrNull(),
            )
        },
        PlaceParserPack("""^Webinar$""") {
            createOnlinePlace(
                title = "Онлайн лекция в Webinar",
                url = it.firstOrNull(),
            )
        },
        PlaceParserPack("""^Online\sкурс$""") {
            createOnlinePlace(
                title = "Онлайн курс",
                url = it.firstOrNull(),
            )
        },
        PlaceParserPack("""^Онлайн$""") {
            createOnlinePlace(
                title = "Онлайн курс",
                url = it.firstOrNull(),
            )
        },
        PlaceParserPack(*otherMap.keys.toTypedArray()) {
            val title = this.value

            val description1 =
                otherMap.toList().firstNotNullOf { (key, value) ->
                    val regex = Regex(key, RegexOption.IGNORE_CASE)
                    if (regex.containsMatchIn(title)) {
                        regex.replace(title, value)
                    } else {
                        null
                    }
                }

            createOtherPlace(
                title = title,
                additionalDescription = description1,
            )
        },
    )

private fun createBuildingPlace(
    title: String,
    areaAlias: String? = null,
    street: String? = null,
    building: String? = null,
    floor: String? = null,
    auditorium: String? = null,
    coordinates: Coordinates? = null,
    // TODO Использовать это
    additionalDescription: String? = null,
): PlaceInfo.Building {
    val description =
        buildString {
            street?.let {
                append(street)
            }

            building?.let {
                ifNotEmpty { append(", ") }
                append("корп. $building")
            }

            floor?.let {
                ifNotEmpty { append(", ") }
                append("$floor-й этаж")
            }
        }

    return PlaceInfo.Building(
        id = "",
        title = title,
        description = description,
        areaAlias = areaAlias,
        street = street,
        building = building,
        floor = floor,
        auditorium = auditorium,
        coordinates = coordinates,
    )
}

private fun createOnlinePlace(
    title: String,
    url: String? = null,
): PlaceInfo.Online {
    val description =
        buildString {
            url?.let {
                append(url)
            }
        }.ifEmpty { null }

    return PlaceInfo.Online(
        id = "",
        title = title,
        description = description,
        url = url,
    )
}

private fun createOtherPlace(
    title: String,
    // TODO Использовать это
    additionalDescription: String? = null,
): PlaceInfo.Other {
    val description =
        buildString {
            additionalDescription?.let {
                append(additionalDescription)
            }
        }

    return PlaceInfo.Other(
        id = "",
        title = title,
        description = description,
    )
}
