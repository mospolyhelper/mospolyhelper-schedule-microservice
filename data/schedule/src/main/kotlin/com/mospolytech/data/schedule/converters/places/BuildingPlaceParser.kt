package com.mospolytech.data.schedule.converters.places

import com.mospolytech.domain.base.model.Coordinates
import com.mospolytech.domain.base.utils.capitalized
import com.mospolytech.domain.base.utils.ifNotEmpty
import com.mospolytech.domain.schedule.model.place.PlaceInfo

internal val buildingPlaceParserChain =
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
        PlaceParserPack("""^пр\sакт\.\sзал$""") {
            createBuildingPlace(
                title = groupValues[0],
                areaAlias = "Прянишникова",
                street = "ул. Прянишникова, 2А",
                building = "1",
                floor = "1",
                auditorium = "Актовый зал",
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
    )

internal fun createBuildingPlace(
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
