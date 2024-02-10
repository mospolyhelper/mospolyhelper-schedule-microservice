package com.mospolytech.data.schedule.converters.places

import com.mospolytech.domain.schedule.model.place.PlaceInfo

internal data class PlaceParserPack(
    val patterns: List<String>,
    val placeFactory: MatchResult.(List<String>) -> PlaceInfo,
) {
    constructor(vararg patterns: String, placeFactory: MatchResult.(List<String>) -> PlaceInfo) :
        this(patterns.toList(), placeFactory)
}

internal val parserChain =
    buildingPlaceParserChain +
        gymPlaceParserChain +
        onlinePlaceParserChain +
        otherPlaceParserChain
