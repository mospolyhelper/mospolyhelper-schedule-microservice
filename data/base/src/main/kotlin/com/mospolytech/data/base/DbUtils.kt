package com.mospolytech.data.base

import com.mospolytech.domain.base.model.PagingDTO
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder

fun <T : Entity<ID>, ID : Comparable<ID>> EntityClass<ID, T>.upsert(value: ID, init: T.() -> Unit): T {
    val existed = findById(value)
    return existed?.apply(init) ?: new(value, init)
}

fun <T : Entity<ID>, ID : Comparable<ID>> EntityClass<ID, T>.insertIfNotExist(
    op: SqlExpressionBuilder.() -> Op<Boolean>,
    init: T.() -> Unit,
): T {
    val existed = find(op).firstOrNull()
    return existed ?: new(init)
}

inline fun <T> createPagingDto(pageSize: Int, page: Int, listBuilder: (Int) -> List<T>): PagingDTO<T> {
    val offset = (page - 1) * pageSize
    val previousPage = if (page <= 1) null else page - 1
    val nextPage = if (page <= 1) 2 else page + 1

    val list = listBuilder(offset)

    return PagingDTO<T>(
        count = list.size,
        previousPage = previousPage,
        nextPage = nextPage,
        data = list,
    )
}
