package com.mospolytech.data.schedule.converters.places

import com.mospolytech.domain.schedule.model.place.PlaceInfo

internal val onlinePlaceParserChain =
    listOf(
        PlaceParserPack(
            """^(LMS|ЛМС)$""",
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
        PlaceParserPack("""^[ОO][нH][-]?лайн$""") {
            createOnlinePlace(
                title = "Онлайн курс",
                url = it.firstOrNull(),
            )
        },
    )

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
