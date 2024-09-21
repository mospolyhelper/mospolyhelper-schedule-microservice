package com.mospolytech.data.base

import com.mospolytech.domain.base.model.PagingDTO
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.CustomStringFunction
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.FieldSet
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Table.Dual.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.stringParam

fun <T : Entity<ID>, ID : Comparable<ID>> EntityClass<ID, T>.upsert(
    id: ID,
    init: T.() -> Unit,
): T {
    val existed = findById(id)
    return existed?.apply(init) ?: new(id, init)
}

inline fun <T : Entity<ID>, ID : Comparable<ID>> EntityClass<ID, T>.upsert(
    noinline op: SqlExpressionBuilder.() -> Op<Boolean>,
    newId: () -> ID,
    noinline init: T.() -> Unit,
): T {
    val existed = find(op).firstOrNull()
    return existed?.apply(init) ?: new(newId(), init)
}

inline fun <T : Entity<ID>, ID : Comparable<ID>> EntityClass<ID, T>.upsert(
    id: ID,
    noinline findOp: SqlExpressionBuilder.() -> Op<Boolean>,
    newId: () -> ID,
    noinline init: T.() -> Unit,
): T {
    val existed = findById(id) ?: find(findOp).firstOrNull()
    return existed?.apply(init) ?: new(newId(), init)
}

fun <T : Entity<ID>, ID : Comparable<ID>> EntityClass<ID, T>.insertIfNotExist(
    op: SqlExpressionBuilder.() -> Op<Boolean>,
    init: T.() -> Unit,
): T {
    val existed = find(op).firstOrNull()
    return existed ?: new(init)
}

inline fun <T> createPagingDto(
    pageSize: Int,
    page: Int,
    listBuilder: (Int) -> List<T>,
): PagingDTO<T> {
    val offset = (page - 1) * pageSize
    val list = listBuilder(offset)

    val previousPage = if (page <= 1) null else page - 1
    val nextPage =
        if (list.size == pageSize) {
            if (page <= 1) 2 else page + 1
        } else {
            null
        }

    return PagingDTO<T>(
        count = list.size,
        previous = previousPage?.toString(),
        next = nextPage?.toString(),
        data = list,
    )
}

fun <ID : Comparable<ID>, T : Entity<ID>> EntityClass<ID, T>.findOrAllIfEmpty(
    query: String,
    op: SqlExpressionBuilder.() -> Op<Boolean>,
): SizedIterable<T> {
    return if (query.isEmpty()) {
        all()
    } else {
        find(op)
    }
}

fun FieldSet.selectOrSelectAllIfEmpty(
    query: String,
    where: SqlExpressionBuilder.() -> Op<Boolean>,
): Query {
    return if (query.isEmpty()) {
        selectAll()
    } else {
        selectAll().where(where)
    }
}

fun FieldSet.selectOrSelectAllIfEmpty(
    columns: List<Expression<*>>,
    query: String,
    where: SqlExpressionBuilder.() -> Op<Boolean>,
): Query {
    return if (query.isEmpty()) {
        select(columns)
    } else {
        select(columns).where(where)
    }
}

fun Column<String>.replace(
    oldValue: String,
    newValue: String,
): Expression<String?> {
    return CustomStringFunction(
        "REPLACE",
        this,
        stringParam(oldValue),
        stringParam(newValue),
    )
}
