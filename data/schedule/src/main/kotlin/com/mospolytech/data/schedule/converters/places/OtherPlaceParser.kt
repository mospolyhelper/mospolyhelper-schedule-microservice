package com.mospolytech.data.schedule.converters.places

import com.mospolytech.domain.schedule.model.place.PlaceInfo

private val otherMap =
    mapOf(
        """^ИМАШ(\sРАН)?$""" to """Институт машиноведения имени А. А. Благонравова РАН""",
        """^ИОНХ(\sРАН)?$""" to """Институт общей и неорганической химии им. Н.С. Курнакова РАН""",
        """^(.*Биоинженерии.*(РАН)?)$""" to """$1""",
        """^(.*Техноград.*)$""" to """Техноград на ВДНХ""",
        """^МИСиС$""" to """НИТУ МИСиС""",
        """^Практика$""" to """Практика""",
        """^Бизнес.кар$""" to """Группа компаний «БИЗНЕС КАР»""",
    )

internal val otherPlaceParserChain =
    listOf(
        PlaceParserPack(
            """^Ц?ПД$""",
            """^Проектная\sдеятельность$""",
        ) {
            createOtherPlace(
                title = "Проектная деятельность",
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
