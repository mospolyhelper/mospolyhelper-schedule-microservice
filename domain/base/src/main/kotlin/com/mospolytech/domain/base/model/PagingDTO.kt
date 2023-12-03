package com.mospolytech.domain.base.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagingDTO<T>(
    @SerialName("count")
    val count: Int,
    @SerialName("previous")
    val previous: String?,
    @SerialName("next")
    val next: String?,
    @SerialName("data")
    val data: List<T>,
) {
    companion object {
        fun <T> from(items: List<T>): PagingDTO<T> {
            return PagingDTO(
                count = items.size,
                previous = null,
                next = null,
                data = items,
            )
        }
    }
}
