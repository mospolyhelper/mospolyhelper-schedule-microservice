package com.mospolytech.domain.base.utils

import com.mospolytech.domain.base.model.PagingDTO

inline fun <T, R> PagingDTO<T>.map(transform: (T) -> R): PagingDTO<R> {
    return PagingDTO<R>(
        count = this.count,
        previous = this.previous,
        next = this.next,
        data = this.data.map(transform),
    )
}
