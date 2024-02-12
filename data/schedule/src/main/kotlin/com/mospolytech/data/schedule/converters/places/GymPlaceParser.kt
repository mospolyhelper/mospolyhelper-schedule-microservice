package com.mospolytech.data.schedule.converters.places

import com.mospolytech.domain.base.model.Coordinates

internal val gymPlaceParserChain =
    listOf(
        PlaceParserPack(
            """^Зал\s+№*1$""",
        ) {
            // Спортивный зал №1 ул. Большая Семеновская, 38, пом. 112а
            val coordinates = Coordinates(55.837495, 37.532223)
            val street = "Большая Семёновская ул., 38"
            val description1 = "Футбол/футзал"
            val description = "Учебные и тренировочные занятия: $description1"

            createBuildingPlace(
                title = "Спортзал №1 (БС)",
                areaAlias = "Спортивный зал №1",
                street = street,
                coordinates = coordinates,
                additionalDescription = description,
            )
        },
        PlaceParserPack(
            """^Зал\s+№*2$""",
            """^Зал\s+№*2\sкорп\.\sБ$""",
        ) {
            // Спортивный зал №2 ул. Семеновская, 38, корпус Б, цокольный этаж
            val coordinates = Coordinates(55.781074, 37.712677)
            val street = "Большая Семёновская ул., 38"
            val description1 = "Тренажерный зал"
            val description = "Учебные и тренировочные занятия: $description1"

            createBuildingPlace(
                title = "Спортзал №2 (БС)",
                areaAlias = "Спортивный зал №2",
                street = street,
                coordinates = coordinates,
                additionalDescription = description,
            )
        },
        PlaceParserPack(
            """^Зал\s+№*3$""",
            """^Зал\s+№*3\s\(МС\)$""",
        ) {
            // Спортивный зал №3 ул. Малая Семеновская, 12
            val coordinates = Coordinates(55.819287, 37.664276)
            val street = "Малая Семёновская ул., 12"
            val description1 = "Единоборства, смешанные боевые единоборства (ММА)"
            val description = "Учебные и тренировочные занятия: $description1"

            createBuildingPlace(
                title = "Спортзал №3 (МС)",
                areaAlias = "Спортивный зал №3",
                street = street,
                coordinates = coordinates,
                additionalDescription = description,
            )
        },
        PlaceParserPack(
            """^Зал\s+№*4$""",
            """^Зал\s+№*4\s\(ПК\)$""",
        ) {
            // Спортивный зал №4 ул. Павла Корчагина, 22, ауд.103
            val coordinates = Coordinates(55.819287, 37.664276)
            val street = "ул. Павла Корчагина, 22"
            val description1 = "Тренажерный зал, Пауэрлифтинг, Кроссфит"
            val description = "Учебные и тренировочные занятия: $description1"

            createBuildingPlace(
                title = "Спортзал №4 (ПК)",
                areaAlias = "Спортивный зал №4",
                street = street,
                coordinates = coordinates,
                additionalDescription = description,
            )
        },
        PlaceParserPack(
            """^Зал\s+№*5$""",
            """^Зал\s+№*5\s\(ПК\)$""",
        ) {
            // Спортивный зал №5 ул. Павла Корчагина, 22, ауд.102
            val coordinates = Coordinates(55.819287, 37.664276)
            val street = "ул. Павла Корчагина, 22"
            val description1 = "Бокс/кикбоксинг"
            val description = "Учебные и тренировочные занятия: $description1"

            createBuildingPlace(
                title = "Спортзал №5 (ПК)",
                areaAlias = "Спортивный зал №5",
                street = street,
                coordinates = coordinates,
                additionalDescription = description,
            )
        },
        PlaceParserPack(
            """^Зал\s+№*6$""",
            """^Зал\s+№*6\s\(БГ\)$""",
        ) {
            // Спортивный зал №6 ул. Бориса Галушкина, 9, 2 этаж
            val coordinates = Coordinates(55.819287, 37.664276)
            val street = "ул. Бориса Галушкина, 9"
            val description1 = "Тренажерный зал, Волейбол"
            val description = "Учебные и тренировочные занятия: $description1"

            createBuildingPlace(
                title = "Спортзал №6 (БГ)",
                areaAlias = "Спортивный зал №6",
                street = street,
                coordinates = coordinates,
                additionalDescription = description,
            )
        },
        PlaceParserPack(
            """^Зал\s+№*7$""",
            """^Зал\s+№*7\s\(ПК\)$""",
        ) {
            // Спортивный зал №7 ул. Павла Корчагина, 22, пом. No519
            val coordinates = Coordinates(55.819287, 37.664276)

            val street = "ул. Павла Корчагина, 22"

            val description1 = "Капоэйра"
            val description = "Учебные и тренировочные занятия: $description1"

            createBuildingPlace(
                title = "Спортзал №7 (ПК)",
                areaAlias = "Спортивный зал №7",
                street = street,
                coordinates = coordinates,
                additionalDescription = description,
            )
        },
        PlaceParserPack(
            """^Зал\s+№*9$""",
            """^Зал\s+№*9[_]*$""",
        ) {
            // Спортивный зал №9 ул. Автозаводская, 16, стр. 3, этаж 5, ауд. 3501

            val coordinates = Coordinates(55.819287, 37.664276)

            val street = "Автозаводская ул., 16"

            val description1 = "Оздоровительная физическая культура"
            val description = "Учебные и тренировочные занятия: $description1"

            createBuildingPlace(
                title = "Спортзал №9",
                areaAlias = "Спортивный зал №9",
                street = street,
                coordinates = coordinates,
                additionalDescription = description,
            )
        },
        PlaceParserPack(
            """^м[\s\p{P}]*спорт[\s\p{P}]*зал$""",
            """^м[\s\p{P}]*спорт[\s\p{P}]*зал\s\(графики\sкафедры\)$""",
        ) {
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
        PlaceParserPack("""^Спорт[\s\p{P}]*зал$""") {
            createBuildingPlace(
                title = "Спортзал",
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
    )
