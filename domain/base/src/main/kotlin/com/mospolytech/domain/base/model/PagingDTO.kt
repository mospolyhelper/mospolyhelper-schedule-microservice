package com.mospolytech.domain.base.model

import kotlinx.serialization.Serializable

@Serializable
data class PagingDTO<T>(
    val count: Int,
    val previousPage: Int?,
    val nextPage: Int?,
    val data: List<T>
) {
    fun<R> map(mapper: (T) -> R): PagingDTO<R> {
        return PagingDTO(
            count, previousPage, nextPage, data.map(mapper::invoke)
        )
    }
}